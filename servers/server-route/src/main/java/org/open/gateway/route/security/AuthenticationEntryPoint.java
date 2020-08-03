package org.open.gateway.route.security;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关认证异常处理,记录日志
 *
 * @author miko
 */
@Slf4j
public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return WebExchangeUtil.writeErrorResponse(HttpStatus.UNAUTHORIZED, exchange, e);
    }

}
