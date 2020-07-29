package org.open.gateway.route.security.filter;

import lombok.AllArgsConstructor;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/21.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class AccessLogFilter implements WebFilter {

    private final AccessLogsService accessLogService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .doOnSuccess(sub -> this.accessLogService.sendAccessLogs(exchange, null).subscribe());
    }
}
