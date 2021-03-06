package org.open.gateway.route.repositories.impl;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.GatewayConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.common.utils.Dates;
import org.open.gateway.common.utils.IdUtil;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.repositories.RefreshableTokenRepository;
import org.open.gateway.route.service.ClientDetailsService;
import org.open.gateway.route.service.bo.ClientDetails;
import org.open.gateway.route.service.bo.OauthClientToken;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/8/6.
 *
 * @author MIKO
 */
@Slf4j
@Component
@AllArgsConstructor
public class R2dbcTokenRepository implements RefreshableTokenRepository {

    private final ClientDetailsService clientDetailsService;
    private final DatabaseClient databaseClient;
    private final ReactiveStringRedisTemplate redisTemplate;

    /**
     * 生成token
     *
     * @param clientDetails 客户端信息
     * @return access_token
     */
    @Override
    public Mono<AccessToken> generate(ClientDetails clientDetails) {
        // token用户信息
        TokenUser tokenUser = toTokenUser(clientDetails);
        // 获取过期时间点
        long expireAt = getExpireAt(clientDetails.getAccessTokenValiditySeconds());
        // 生成token
        return generateToken(expireAt, tokenUser)
                .map(token -> this.toAccessToken(token, expireAt));
    }

    /**
     * 根据客户端id获取token信息
     *
     * @param clientId 客户端id
     * @return token信息
     */
    @Override
    public Mono<OauthClientToken> loadNotExpiredClientTokenByClientId(String clientId) {
        return databaseClient.execute(SQLS.QUERY_CLIENT_TOKEN_BY_ID.format(clientId))
                .map(row -> this.rowToClientToken(row, clientId))
                .one();
    }

    /**
     * 保存token
     *
     * @param clientId   客户端id
     * @param token      token值
     * @param expireTime 过期时间
     */
    @Override
    public Mono<Void> saveClientToken(String clientId, String token, Long expireTime) {
        Assert.notNull(clientId, "client_id is required");
        Assert.notNull(token, "token is required");
        Assert.notNull(expireTime, "expireTime is required");
        return databaseClient.insert()
                .into("oauth_client_token")
                .value("client_id", clientId)
                .value("token", token)
                .value("expire_time", Dates.toLocalDateTime(expireTime))
                .value("create_time", LocalDateTime.now())
                .value("create_person", "system")
                .value("update_time", LocalDateTime.now())
                .value("update_person", "system")
                .fetch()
                .rowsUpdated() // 添加一条新的token记录
                .then()
                .doOnSuccess(v -> log.info("Save token finished."));
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        if (param.getArgs() == null || param.getArgs().isEmpty()) {
            log.info("[Refresh client token] param is empty no need for refresh.");
            return Mono.empty();
        }
        return Flux.fromIterable(param.getArgs())
                .flatMap(clientId ->
                        Mono.zip(
                                clientDetailsService.loadClientByClientId(clientId),
                                loadNotExpiredClientTokenByClientId(clientId)
                        ).flatMap(tuple2 -> {
                            ClientDetails clientDetails = tuple2.getT1();
                            OauthClientToken token = tuple2.getT2();
                            // 重置token持续时间
                            return storeToken(getExpireAt(clientDetails.getAccessTokenValiditySeconds()), toTokenUser(clientDetails), token.getToken());
                        }).doOnSuccess(b -> log.info("[Refresh client token] finished."))
                ).then();
    }

    /**
     * 生成token
     *
     * @param expireAt  过期时间
     * @param tokenUser 客户端信息
     * @return 生成的token
     */
    private Mono<String> generateToken(long expireAt, TokenUser tokenUser) {
        // 生成授权访问token
        String token = IdUtil.uuid();
        // 生成token放入redis中
        return storeToken(expireAt, tokenUser, token)
                .thenReturn(token);
    }

    /**
     * 存储token
     *
     * @param expireAt  过期时间
     * @param tokenUser token用户
     * @param token     token字符串
     * @return 存储结果
     */
    private Mono<Boolean> storeToken(long expireAt, TokenUser tokenUser, String token) {
        return this.redisTemplate.opsForValue()
                .set(GatewayConstants.RedisKey.PREFIX_ACCESS_TOKENS + token, JSON.toJSONString(tokenUser), Duration.ofMillis(expireAt).minusMillis(System.currentTimeMillis()))
                .doOnSuccess(b -> log.info("Store token finished. result:{}", b));
    }

    /**
     * 获取过期时间点
     *
     * @param accessTokenValiditySeconds 有效时间
     * @return 过期时间点
     */
    private long getExpireAt(long accessTokenValiditySeconds) {
        return Duration.ofSeconds(accessTokenValiditySeconds)
                .plusMillis(Dates.toTimestamp(LocalDateTime.now()))
                .toMillis();
    }

    /**
     * 生成accessToken
     *
     * @param token    token字符串
     * @param expireAt 过期时间点
     * @return accessToken对象
     */
    private AccessToken toAccessToken(String token, long expireAt) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setExpireAt(expireAt);
        return accessToken;
    }

    /**
     * 生成token用户信息
     *
     * @param clientDetails 客户端配置信息
     * @return token用户信息
     */
    private TokenUser toTokenUser(ClientDetails clientDetails) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClientId(clientDetails.getClientId());
        if (clientDetails.getAuthorities() != null) {
            tokenUser.setAuthorities(
                    clientDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet())
            );
        }
        tokenUser.setScopes(clientDetails.getScope());
        return tokenUser;
    }

    private OauthClientToken rowToClientToken(Row row, String clientId) {
        OauthClientToken token = new OauthClientToken();
        token.setClientId(clientId);
        token.setId(Objects.requireNonNull(row.get("id", Integer.class)));
        token.setToken(Objects.requireNonNull(row.get("token", String.class)));
        token.setExpireTime(Objects.requireNonNull(row.get("expire_time", LocalDateTime.class)));
        return token;
    }

}

