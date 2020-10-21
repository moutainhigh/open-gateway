package org.open.gateway.portal.security;

import lombok.AllArgsConstructor;
import org.open.gateway.portal.modules.account.srevice.TokenService;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.open.gateway.portal.security.exception.InvalidTokenException;
import org.open.gateway.portal.security.exception.TokenExpiredException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisTokenAuthenticationConverter implements AuthenticationConverter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private final TokenService tokenService;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        BaseAccountBO account = loadAccount(token);
        return new RedisTokenAuthentication(account);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            throw new InvalidTokenException();
        }
        return token.substring(TOKEN_PREFIX.length());
    }

    private BaseAccountBO loadAccount(String token) {
        BaseAccountBO account = tokenService.loadTokenUser(token);
        if (account == null) {
            throw new TokenExpiredException();
        }
        return account;
    }

}
