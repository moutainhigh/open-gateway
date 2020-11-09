package org.open.gateway.base.constants;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
public interface MqConstants {

    // exchange-刷新路由
    String EXCHANGE_REFRESH_ROUTES = "refresh_routes.fanout";
    // exchange-刷新黑白名单
    String EXCHANGE_REFRESH_IP_LIMITS = "refresh_ip_limits.fanout";

    // exchange-开放网关
    String EXCHANGE_OPEN_GATEWAY_DIRECT = "open_gateway.direct";

    // routing_key-刷新客户端token
    String ROUTING_KEY_REFRESH_CLIENT_TOKEN = "refresh_client_token";
    // queue-刷新客户端token
    String QUEUE_REFRESH_CLIENT_TOKEN = "refresh_client_token";

    // routing_key-网关访问日志
    String ROUTING_KEY_GATEWAY_ACCESS_LOGS = "gateway_access_logs";
    // queue-网关访问日志
    String QUEUE_GATEWAY_ACCESS_LOGS = "gateway_access_logs";

}
