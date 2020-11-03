package org.open.gateway.portal.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.OAuth2Constants;
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
@Slf4j
@AllArgsConstructor
public class RedisTokenAuthenticationConverter implements AuthenticationConverter {

    private static final Pattern TOKEN_REGX = Pattern.compile(OAuth2Constants.TOKEN_PREFIX + "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");
    private final TokenService tokenService;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        AccountDetails account = loadAccount(token);
        return new RedisTokenAuthentication(account);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("get token value:{} from request header:{}", token, HttpHeaders.AUTHORIZATION);
        if (token == null || !TOKEN_REGX.matcher(token).matches()) {
            log.debug("token:{} is invalid", token);
            throw new InvalidTokenException();
        }
        return token.substring(OAuth2Constants.TOKEN_PREFIX.length());
    }

    private AccountDetails loadAccount(String token) {
        AccountDetails account = tokenService.loadTokenUser(token);
        log.debug("load token user:{} by token:{}", account, token);
        if (account == null) {
            throw new TokenExpiredException();
        }
        return account;
    }

}
