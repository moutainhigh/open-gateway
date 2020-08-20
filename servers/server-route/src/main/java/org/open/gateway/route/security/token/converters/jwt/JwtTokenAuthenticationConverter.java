package org.open.gateway.route.security.token.converters.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import open.gateway.common.base.constants.OAuth2Constants;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.exception.InvalidTokenException;
import org.open.gateway.route.security.token.AuthenticationToken;
import org.open.gateway.route.security.token.converters.AbstractBearerTokenAuthenticationConverter;
import org.open.gateway.route.utils.jwt.Jwts;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/8.
 * 解析请求头中的token信息
 *
 * @author MIKO
 */
@AllArgsConstructor
public class JwtTokenAuthenticationConverter extends AbstractBearerTokenAuthenticationConverter {

    private final Jwts jwtDecoder;

    @Override
    protected Mono<Authentication> parseToken(String token) {
        if (token == null || token.isEmpty()) {
            throw invalidTokenError();
        }
        JWTClaimsSet jwtClaimsSet = this.jwtDecoder.parseToken(token);
        return Mono.just(new AuthenticationToken(toTokenUser(jwtClaimsSet)));
    }

    private TokenUser toTokenUser(JWTClaimsSet claims) {
        if (claims == null) {
            throw new InvalidTokenException();
        }
        if (claims.getClaim(OAuth2Constants.TokenPayloadKey.SUB) == null) {
            throw new InvalidTokenException();
        }
        if (claims.getClaim(OAuth2Constants.TokenPayloadKey.AUTHORITIES) == null) {
            throw new InvalidTokenException();
        }
        Object roles = claims.getClaim(OAuth2Constants.TokenPayloadKey.AUTHORITIES);
        if (!(roles instanceof JSONArray)) {
            throw new InvalidTokenException();
        }
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClientId(claims.getClaim(OAuth2Constants.TokenPayloadKey.SUB).toString());
        tokenUser.setAuthorities(
                ((JSONArray) roles).stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.toList())
        );
        Object scopes = claims.getClaim(OAuth2Constants.TokenPayloadKey.SCOPE);
        if (scopes != null) {
            if (!(scopes instanceof JSONArray)) {
                throw new InvalidTokenException();
            }
            tokenUser.setScopes(
                    ((JSONArray) scopes).stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.toList())
            );
        }
        return tokenUser;
    }

}
