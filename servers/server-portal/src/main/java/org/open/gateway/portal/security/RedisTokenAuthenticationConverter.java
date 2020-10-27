package org.open.gateway.portal.security;

import lombok.AllArgsConstructor;
import org.open.gateway.portal.modules.account.service.TokenService;
import org.open.gateway.portal.security.exception.InvalidTokenException;
import org.open.gateway.portal.security.exception.TokenExpiredException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisTokenAuthenticationConverter implements AuthenticationConverter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final Pattern TOKEN_REGX = Pattern.compile(TOKEN_PREFIX + "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");
    private final TokenService tokenService;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        AccountDetails account = loadAccount(token);
        return new RedisTokenAuthentication(account);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !TOKEN_REGX.matcher(token).matches()) {
            throw new InvalidTokenException();
        }
        return token.substring(TOKEN_PREFIX.length());
    }

    private AccountDetails loadAccount(String token) {
        AccountDetails account = tokenService.loadTokenUser(token);
        if (account == null) {
            throw new TokenExpiredException();
        }
        return account;
    }

}
