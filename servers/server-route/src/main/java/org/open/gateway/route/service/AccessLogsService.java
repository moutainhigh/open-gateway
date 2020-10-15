package org.open.gateway.route.service;

import open.gateway.common.base.entity.AccessLogs;
import org.springframework.web.server.ServerWebExchange;

/**
 * Created by miko on 2020/7/20.
 *
 * @author MIKO
 */
public interface AccessLogsService {

    /**
     * 发送操作日志
     *
     * @param exchange web信息
     */
    void sendAccessLogs(ServerWebExchange exchange);

    /**
     * 发送操作日志
     *
     * @param exchange web信息
     * @param ex       异常信息
     */
    void sendAccessLogs(ServerWebExchange exchange, Throwable ex);

    /**
     * 发送操作日志
     *
     * @param accessLogs 操作日志
     */
    void sendAccessLogs(AccessLogs accessLogs);

}
