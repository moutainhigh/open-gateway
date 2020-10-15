package org.open.gateway.route.repositories.r2dbc;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.utils.Dates;
import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.repositories.TokenRepository;
import org.open.gateway.route.security.token.generators.TokenGenerator;
import org.open.gateway.route.service.bo.ClientDetails;
import org.open.gateway.route.service.bo.OauthClientToken;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Update;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;
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
@Service
@AllArgsConstructor
public class R2dbcTokenRepository implements TokenRepository {

    private final TransactionalOperator operator;
    private final DatabaseClient databaseClient;
    private final TokenGenerator generator;

    /**
     * 生成token
     *
     * @param clientDetails 客户端信息
     * @return access_token
     */
    @Override
    public Mono<AccessToken> generate(ClientDetails clientDetails) {
        // token用户信息
        TokenUser tokenUser = generateTokenUser(clientDetails);
        // 获取过期时间点
        long expireAt = getExpireAt(clientDetails.getAccessTokenValiditySeconds());
        // 生成token
        return this.generator.generate(expireAt, tokenUser)
                .map(token -> this.generateAccessToken(token, expireAt));
    }

    /**
     * 获取过期时间点
     *
     * @param accessTokenValiditySeconds 有效时间
     * @return 过期时间点
     */
    private long getExpireAt(long accessTokenValiditySeconds) {
        return Duration.ofSeconds(accessTokenValiditySeconds)
                .plusMillis(System.currentTimeMillis())
                .toMillis();
    }

    /**
     * 生成accessToken
     *
     * @param token    token字符串
     * @param expireAt 过期时间点
     * @return accessToken对象
     */
    private AccessToken generateAccessToken(String token, long expireAt) {
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
    private TokenUser generateTokenUser(ClientDetails clientDetails) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClientId(clientDetails.getClientId());
        if (clientDetails.getAuthorities() != null && !clientDetails.getAuthorities().isEmpty()) {
            tokenUser.setAuthorities(
                    clientDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            );
        }
        tokenUser.setScopes(clientDetails.getScope());
        return tokenUser;
    }

    /**
     * 根据客户端id获取token信息(不为null)
     *
     * @param clientId 客户端id
     * @return token信息
     */
    @Override
    public Mono<OauthClientToken> loadClientTokenByClientId(String clientId) {
        return databaseClient.execute(SQLS.QUERY_CLIENT_TOKEN_BY_ID.format(clientId))
                .map(this::rowToClientToken)
                .one();
    }

    /**
     * 根据客户端id删除token
     *
     * @param clientId 客户端id
     */
    @Override
    public Mono<Integer> deleteClientTokenByClientId(String clientId) {
        return databaseClient.update()
                .table("oauth_client_token")
                .using(Update.update("is_del", 1))
                .matching(
                        Criteria.where("client_id").is(clientId).and("is_del").is(0)
                )
                .fetch()
                .rowsUpdated();
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
        return deleteClientTokenByClientId(clientId) // 先根据客户端id更新老的token为已删除状态
                .then(
                        databaseClient.insert()
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
                )
                .as(operator::transactional) // 开启事务管理
                .doOnSuccess(v -> log.info("Save token finished"));
    }

    private OauthClientToken rowToClientToken(Row row) {
        OauthClientToken token = new OauthClientToken();
        token.setId(Objects.requireNonNull(row.get("id", Integer.class)));
        token.setClientId(Objects.requireNonNull(row.get("client_id", String.class)));
        token.setToken(Objects.requireNonNull(row.get("token", String.class)));
        token.setExpireTime(Objects.requireNonNull(row.get("expire_time", LocalDateTime.class)));
        return token;
    }

}

