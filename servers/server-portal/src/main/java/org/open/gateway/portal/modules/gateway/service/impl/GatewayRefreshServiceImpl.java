package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.portal.modules.gateway.service.GatewayRefreshService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by miko on 2020/7/16.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Service
public class GatewayRefreshServiceImpl implements GatewayRefreshService {

    private final AmqpTemplate rabbitTemplate;

    /**
     * 刷新路由
     *
     * @param apiCodes api代码
     */
    @Override
    public void refreshRoutes(Set<String> apiCodes) {
        log.info("starting send refresh route msg. api codes:{}", apiCodes);
        // 发送刷新网关消息
        this.rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_REFRESH_ROUTES, "", new RefreshGateway(apiCodes));
        log.info("finished send refresh route msg.");
    }

    /**
     * 刷新客户端资源关系
     *
     * @param clientIds 客户端id
     */
    @Override
    public void refreshClientToken(Set<String> clientIds) {
        log.info("starting send refresh client token msg. client ids:{}", clientIds);
        // 发送刷新网关消息
        this.rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_OPEN_GATEWAY_DIRECT, MqConstants.ROUTING_KEY_REFRESH_CLIENT_TOKEN, new RefreshGateway(clientIds));
        log.info("finished send refresh client token msg.");
    }

    /**
     * 刷新黑白名单
     *
     * @param ipLimits ip限制id
     */
    @Override
    public void refreshIpLimits(Set<String> ipLimits) {
        log.info("starting send refresh resources msg. ip limits:{}", ipLimits);
        this.rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_REFRESH_IP_LIMITS, "", new RefreshGateway(ipLimits));
        log.info("finished send refresh ip limits msg.");
    }

}
