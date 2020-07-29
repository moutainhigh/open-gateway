package org.open.gateway.route.security.token.generators.redis;

import lombok.AllArgsConstructor;
import open.gateway.common.base.constants.GatewayConstants;
import open.gateway.common.base.constants.OAuth2Constants;
import open.gateway.common.base.entity.oauth2.OAuth2TokenRequest;
import open.gateway.common.base.entity.token.AccessToken;
import open.gateway.common.base.entity.token.TokenUser;
import open.gateway.common.utils.IdUtil;
import open.gateway.common.utils.JSON;
import org.open.gateway.route.security.client.ClientDetails;
import org.open.gateway.route.security.token.generators.TokenGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/22.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisClientCredentialsTokenGenerator implements TokenGenerator {

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean isSupported(String grantType) {
        return OAuth2Constants.GrantType.CLIENT_CREDENTIALS.equals(grantType);
    }

    @Override
    public AccessToken generate(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails) {
        AccessToken accessToken = generateAccessToken(clientDetails);
        TokenUser tokenUser = generateTokenUser(clientDetails);
        // 生成token放入redis中
        this.redisTemplate.opsForValue().set(GatewayConstants.RedisKey.PREFIX_ACCESS_TOKENS + accessToken.getToken(), JSON.toJSONString(tokenUser), clientDetails.getAccessTokenValiditySeconds(), TimeUnit.SECONDS);
        return accessToken;
    }

    private AccessToken generateAccessToken(ClientDetails clientDetails) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(IdUtil.uuid());
        accessToken.setExpire_in(clientDetails.getAccessTokenValiditySeconds());
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
