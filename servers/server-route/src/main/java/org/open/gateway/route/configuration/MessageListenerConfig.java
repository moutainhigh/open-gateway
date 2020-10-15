package org.open.gateway.route.configuration;

import org.open.gateway.route.listener.mq.MqClientResourcesMessageListener;
import org.open.gateway.route.listener.mq.MqIpLimitMessageListener;
import org.open.gateway.route.listener.mq.MqRouteDefinitionMessageListener;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by miko on 2020/7/15.
 * 消息监听器配置
 *
 * @author MIKO
 */
@Configuration
public class MessageListenerConfig {

    @Bean
    public MqRouteDefinitionMessageListener routeDefinitionMessageListener(RefreshableRouteDefinitionRepository routeDefinitionRepository) {
        return new MqRouteDefinitionMessageListener(routeDefinitionRepository);
    }

    @Bean
    public MqClientResourcesMessageListener clientResourcesMessageListener(RefreshableClientResourcesRepository clientResourcesRepository) {
        return new MqClientResourcesMessageListener(clientResourcesRepository);
    }

    @Bean
    public MqIpLimitMessageListener ipLimitMessageListener(RefreshableIpLimitRepository ipLimitRepository) {
        return new MqIpLimitMessageListener(ipLimitRepository);
    }

}
