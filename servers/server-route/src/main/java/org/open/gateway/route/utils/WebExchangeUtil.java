package org.open.gateway.route.utils;

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
 *
 * @author MIKO
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
     * 获取请求body数据
     *
     * @param exchange web交换信息
     */
    public static DataBuffer getCachedRequestBody(ServerWebExchange exchange) {
        return (DataBuffer) exchange.getAttributes().remove(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
//        return exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
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
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        String ip = headers.get("X-Forwarded-For");
        if (isIpValid(ip)) {
            ip = headers.get("Proxy-Client-IP");
        }
        if (isIpValid(ip)) {
            ip = headers.get("WL-Proxy-Client-IP");
        }
        if (isIpValid(ip)) {
            ip = headers.get("HTTP_CLIENT_IP");
        }
        if (isIpValid(ip)) {
            ip = headers.get("HTTP_X_FORWARDED_FOR");
        }
        if (isIpValid(ip)) {
            ip = headers.get("X-Real-IP");
        }
        if (isIpValid(ip)) {
            if (request.getRemoteAddress() != null) {
                ip = request.getRemoteAddress().getAddress().getHostAddress();
            }
        }
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
     * ip是否有效
     *
     * @param ip ip地址
     * @return true有效, false无效
     */
    private static boolean isIpValid(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
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
