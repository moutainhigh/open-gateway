package org.open.gateway.route.security.token.generators.redis;

import lombok.AllArgsConstructor;
import open.gateway.common.base.constants.GatewayConstants;
import open.gateway.common.base.constants.OAuth2Constants;
import open.gateway.common.utils.IdUtil;
import open.gateway.common.utils.JSON;
import org.open.gateway.route.entity.oauth2.OAuth2TokenRequest;
import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.security.token.generators.TokenGenerator;
import org.open.gateway.route.service.bo.ClientDetails;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/22.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisClientCredentialsTokenGenerator implements TokenGenerator {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public boolean isSupported(String grantType) {
        return OAuth2Constants.GrantType.CLIENT_CREDENTIALS.equals(grantType);
    }

    @Override
    public Mono<AccessToken> generate(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails) {
        // 生成token用户信息
        TokenUser tokenUser = generateTokenUser(clientDetails);
        // 有效时间, 单位秒
        Duration validitySeconds = Duration.ofSeconds(clientDetails.getAccessTokenValiditySeconds());
        // 生成授权访问token
        AccessToken accessToken = generateAccessToken(validitySeconds);
        // 生成token放入redis中
        return this.redisTemplate.opsForValue()
                .set(GatewayConstants.RedisKey.PREFIX_ACCESS_TOKENS + accessToken.getToken(), JSON.toJSONString(tokenUser), validitySeconds)
                .thenReturn(accessToken);
    }

    private AccessToken generateAccessToken(Duration validitySeconds) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(IdUtil.uuid());
        accessToken.setExpireIn(System.currentTimeMillis() + validitySeconds.toMillis());
        return accessToken;
    }

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

}
