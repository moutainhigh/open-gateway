package org.open.gateway.route.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.AccessLogs;
import org.open.gateway.common.utils.JSON;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.route.service.AccessLogsService;
import org.open.gateway.route.utils.RouteDefinitionUtil;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Objects;

/**
 * Created by miko on 2020/7/20.
 *
 * @author MIKO
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccessLogsServiceImpl implements AccessLogsService {

    private final AmqpTemplate amqpTemplate;

    /**
     * 发送操作日志
     *
     * @param exchange web信息
     */
    @Override
    public void sendAccessLogs(ServerWebExchange exchange) {
        sendAccessLogs(exchange, null);
    }

    /**
     * 发送操作日志
     *
     * @param exchange web信息
     * @param ex       异常信息
     */
    @Override
    public void sendAccessLogs(ServerWebExchange exchange, Throwable ex) {
        AccessLogs accessLogs = buildAccessLogs(exchange, ex);
        sendAccessLogs(accessLogs);
    }

    /**
     * 发送操作日志
     *
     * @param accessLogs 操作日志
     */
    @Override
    public void sendAccessLogs(AccessLogs accessLogs) {
        amqpTemplate.convertAndSend(MqConstants.EXCHANGE_OPEN_GATEWAY_DIRECT, MqConstants.ROUTING_KEY_GATEWAY_ACCESS_LOGS, accessLogs);
        log.info("Send access log finished");
    }

    /**
     * 构建访问日志
     *
     * @param exchange 路由信息
     * @param ex       异常
     * @return 访问日志
     */
    private AccessLogs buildAccessLogs(ServerWebExchange exchange, @Nullable Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        AccessLogs accessLogs = new AccessLogs();
        accessLogs.setPath(request.getURI().getPath());
        accessLogs.setClientId(WebExchangeUtil.getClientId(exchange));
        accessLogs.setIp(WebExchangeUtil.getRemoteAddress(exchange));
        accessLogs.setHttpStatus(Objects.requireNonNull(response.getStatusCode()).value());
        accessLogs.setHttpMethod(request.getMethodValue());
        accessLogs.setHttpHeaders(serializeToString(headers.toSingleValueMap()));
        accessLogs.setRequestQueryString(StringUtil.splitByLenLimit(serializeToString(request.getQueryParams()), 512));
        accessLogs.setRequestBody(readRequestBody(exchange));
        accessLogs.setRequestTime(WebExchangeUtil.getRequestTime(exchange));
        accessLogs.setResponseTime(new Date());
        accessLogs.setUsedTime((int) (accessLogs.getResponseTime().getTime() - accessLogs.getRequestTime().getTime()));
        accessLogs.setUserAgent(headers.getFirst(HttpHeaders.USER_AGENT));
        if (ex != null) {
            accessLogs.setError(StringUtil.splitByLenLimit(ex.getMessage(), 512));
        }
        // 路由信息中获取api信息
        Route route = WebExchangeUtil.getGatewayRoute(exchange);
        if (route != null) {
            accessLogs.setApiCode(RouteDefinitionUtil.getApiCode(route.getMetadata()));
            accessLogs.setRouteCode(RouteDefinitionUtil.getRouteCode(route.getMetadata()));
        }
        // accessLogs.setRegion(); // 根据ip获取用户请求区域
        return accessLogs;
    }

    /**
     * 转字符串
     *
     * @param param 参数
     * @return 字符串
     */
    private String serializeToString(Object param) {
        return JSON.toJSONString(param);
    }

    /**
     * 读取请求参数
     *
     * @param exchange 交换信息
     * @return 请求body参数
     */
    private String readRequestBody(ServerWebExchange exchange) {
        DataBuffer dataBuffer = WebExchangeUtil.getCachedRequestBody(exchange);
        if (dataBuffer == null) {
            log.warn("Cached request body is null");
            return null;
        }
        CharBuffer charBuffer = Charset.defaultCharset().decode(dataBuffer.asByteBuffer());
        DataBufferUtils.release(dataBuffer);
        return charBuffer.toString();
    }

}
