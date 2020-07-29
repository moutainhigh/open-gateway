package org.open.gateway.route.security.filter;

import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/20.
 * 请求前缀过滤器
 *
 * @author MIKO
 */
public class PreRequestFilter implements WebFilter {

    private final ServerCodecConfigurer configurer;

    public PreRequestFilter(ServerCodecConfigurer configurer) {
        this.configurer = configurer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 添加请求时间
        WebExchangeUtil.putRequestTime(exchange);
        return chain.filter(new BodyCacheServerWebExchange(exchange, configurer));

    }


}
