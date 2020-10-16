package org.open.gateway.base.constants;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
public interface MqConstants {

    // exchange-刷新路由
    String EXCHANGE_REFRESH_ROUTES = "refresh_routes";
    // exchange-刷新客户端资源
    String EXCHANGE_REFRESH_CLIENT_RESOURCES = "refresh_client_resources";
    // exchange-刷新黑白名单
    String EXCHANGE_REFRESH_IP_LIMITS = "refresh_ip_limits";
    // exchange-网关访问日志
    String EXCHANGE_GATEWAY_ACCESS_LOGS = "gateway_access_logs";
    // routing_key-网关访问日志
    String ROUTING_KEY_GATEWAY_ACCESS_LOGS = "gateway_access_logs";
    // queue-网关访问日志
    String QUEUE_GATEWAY_ACCESS_LOGS = "gateway_access_logs";

}
