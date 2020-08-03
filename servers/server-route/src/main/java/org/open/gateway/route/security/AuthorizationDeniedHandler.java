package org.open.gateway.route.security;

import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/8/3.
 * 授权失败处理器
 *
 * @author MIKO
 */
public class AuthorizationDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        return WebExchangeUtil.writeErrorResponse(HttpStatus.FORBIDDEN, exchange, e);
    }

}
