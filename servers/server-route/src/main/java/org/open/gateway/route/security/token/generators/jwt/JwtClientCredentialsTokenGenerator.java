package org.open.gateway.route.security.token.generators.jwt;

import lombok.AllArgsConstructor;
import open.gateway.common.base.constants.OAuth2Constants;
import org.open.gateway.route.entity.oauth2.OAuth2TokenRequest;
import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.security.token.generators.TokenGenerator;
import org.open.gateway.route.service.bo.ClientDetails;
import org.open.gateway.route.utils.jwt.Jwts;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;

/**
 * Created by miko on 2020/7/8.
 * 客户端模式token生成器
 *
 * @author MIKO
 */
@AllArgsConstructor
public class JwtClientCredentialsTokenGenerator implements TokenGenerator {

    private final Jwts jwtEncoder;

    @Override
    public boolean isSupported(String grantType) {
        return OAuth2Constants.GrantType.CLIENT_CREDENTIALS.equals(grantType);
    }

    @Override
    public AccessToken generate(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails) {
        return this.jwtEncoder.generateToken(
                clientDetails.getClientId(),
                clientDetails.getAccessTokenValiditySeconds() * 1000,
                getClaims(clientDetails)
        );
    }

    /**
     * 获取要生成jwt token的自定义数据
     *
     * @param clientDetails 客户端
     * @return 自定义数据
     */
    protected HashMap<String, Object> getClaims(ClientDetails clientDetails) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(OAuth2Constants.TokenPayloadKey.SCOPE, clientDetails.getScope());
        map.put(OAuth2Constants.TokenPayloadKey.REDIRECT_URI, clientDetails.getRegisteredRedirectUri());
        if (clientDetails.getAuthorities() != null) {
            map.put(OAuth2Constants.TokenPayloadKey.AUTHORITIES, clientDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray());
        }
        return map;
    }

}
