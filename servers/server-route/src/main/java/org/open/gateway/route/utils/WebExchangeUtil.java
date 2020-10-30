package org.open.gateway.route.utils;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

/**
 * Created by miko on 2020/7/20.
 * <p>
 * ServerWebExchange工具类
 *
 * @author MIKO
 * @see org.springframework.web.server.ServerWebExchange
 */
public class WebExchangeUtil {

    /**
     * 请求时间
     */
    private static final String REQUEST_TIME = "request_time";
    /**
     * 客户端id
     */
    private static final String CLIENT_ID = "client_id";
    /**
     * 请求头中的ip字段
     */
    private static final String[] IP_HEADERS = new String[]{"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP"};

    /**
     * 缓存请求body
     *
     * @param chain    过滤器链
     * @param exchange web交换信息
     * @return mono
     */
    public static Mono<Void> cacheRequestBody(GatewayFilterChain chain, ServerWebExchange exchange) {
        return ServerWebExchangeUtils.cacheRequestBody(exchange, serverHttpRequest -> {
            if (serverHttpRequest == exchange.getRequest()) {
                return chain.filter(exchange);
            }
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    /**
     * 获取请求body数据
     *
     * @param exchange web交换信息
     * @return 请求body
     */
    public static DataBuffer getCachedRequestBody(ServerWebExchange exchange) {
        return (DataBuffer) exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
    }

    /**
     * 获取路由信息
     *
     * @param exchange web交换信息
     * @return 路由信息
     */
    public static Route getGatewayRoute(ServerWebExchange exchange) {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }

    /**
     * 添加请求时间
     *
     * @param exchange web交换信息
     */
    public static void putRequestTime(ServerWebExchange exchange) {
        exchange.getAttributes().put(REQUEST_TIME, new Date());
    }

    /**
     * 获取请求时间
     *
     * @param exchange web交换信息
     * @return 请求时间
     */
    public static Date getRequestTime(ServerWebExchange exchange) {
        return (Date) exchange.getAttributes().get(REQUEST_TIME);
    }

    /**
     * 存放客户端id
     *
     * @param exchange web交换信息
     * @param clientId 客户端id
     */
    public static void putClientId(ServerWebExchange exchange, String clientId) {
        exchange.getAttributes().put(CLIENT_ID, clientId);
    }

    /**
     * 获取客户端id
     *
     * @param exchange web交换信息
     * @return 客户端id
     */
    public static String getClientId(ServerWebExchange exchange) {
        return (String) exchange.getAttributes().get(CLIENT_ID);
    }

    /**
     * 获取IP地址
     *
     * @param exchange 请求参数
     * @return ip地址
     */
    public static String getRemoteAddress(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String ip = getIpFromRequest(request);
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 0) {
            String[] ips = ip.split(",");
            if (ips.length > 0) {
                ip = ips[0];
            }
        }
        return ip;
    }

    /**
     * 从请求信息中获取ip
     *
     * @param request 请求信息
     * @return ip地址
     */
    private static String getIpFromRequest(ServerHttpRequest request) {
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        for (String key : IP_HEADERS) {
            String ip = headers.get(key);
            if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        if (request.getRemoteAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        return null;
    }

    /**
     * 获取请求路径
     *
     * @param exchange web路由交换信息
     * @return 请求路径
     */
    public static String getRequestPath(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().getPath();
    }

    /**
     * 写入response
     *
     * @param status   状态码
     * @param exchange web交换信息
     * @param e        异常信息
     * @return 写入操作
     */
    public static Mono<Void> writeErrorResponse(HttpStatus status, ServerWebExchange exchange, Throwable e) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(status);
                    response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    DataBuffer buffer = dataBufferFactory.wrap(e.getMessage().getBytes(
                            Charset.defaultCharset()));
                    return response.writeWith(Mono.just(buffer))
                            .doOnError(error -> DataBufferUtils.release(buffer));
                });
    }

}
