package org.open.gateway.route.filter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.service.AccessLogsService;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(value = "gateway.log.enable", havingValue = "true", matchIfMissing = true)
public class RequestRecorderGatewayFilter implements GlobalFilter, Ordered {

    private final AccessLogsService accessLogsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        //只记录http的请求
        String scheme = requestURI.getScheme();
        if ((!"http".equals(scheme) && !"https".equals(scheme))) {
            return chain.filter(exchange);
        }
        String upgrade = request.getHeaders().getUpgrade();
        if ("websocket".equalsIgnoreCase(upgrade)) {
            return chain.filter(exchange);
        }
        // 添加请求时间
        WebExchangeUtil.putRequestTime(exchange);
        return WebExchangeUtil.cacheRequestBody(chain, exchange)
                .doOnSuccess(v -> this.accessLogsService.sendAccessLogs(exchange)) // 发送日志
                .doOnError(error -> this.accessLogsService.sendAccessLogs(exchange, error));
    }

    @Override
    public int getOrder() {
        //在GatewayFilter之前执行
        return -1;
    }

}
