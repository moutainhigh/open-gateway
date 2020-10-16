package org.open.gateway.portal.modules.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.portal.modules.gateway.service.GatewayService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by miko on 2020/7/16.
 *
 * @author MIKO
 */
@Slf4j
@Service
@AllArgsConstructor
public class GatewayServiceImpl implements GatewayService {

    private final AmqpTemplate rabbitTemplate;

    /**
     * 刷新路由
     *
     * @param apiCodes api代码
     */
    @Override
    public void refreshRoutes(Set<String> apiCodes) {
        log.info("Starting send refresh route msg. api codes:{}", apiCodes);
        // 发送刷新网关消息
        this.rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_REFRESH_ROUTES, "", new RefreshGateway(apiCodes));
        log.info("Finished send refresh route msg.");
    }

    /**
     * 刷新客户端资源关系
     *
     * @param clientIds 客户端id
     */
    @Override
    public void refreshResources(Set<String> clientIds) {
        log.info("Starting send refresh resources msg. client ids:{}", clientIds);
        // 发送刷新网关消息
        this.rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_REFRESH_CLIENT_RESOURCES, "", new RefreshGateway(clientIds));
        log.info("Finished send refresh resources msg.");
    }

    /**
     * 刷新黑白名单
     *
     * @param ipLimits ip限制id
     */
    @Override
    public void refreshIpLimits(Set<String> ipLimits) {
        this.rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_REFRESH_IP_LIMITS, "", new RefreshGateway(ipLimits));
    }

}
