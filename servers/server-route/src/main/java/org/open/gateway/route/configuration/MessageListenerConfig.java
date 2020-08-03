package org.open.gateway.route.configuration;

import open.gateway.common.base.constants.GatewayConstants;
import org.open.gateway.route.listener.mq.MqClientResourcesMessageListener;
import org.open.gateway.route.listener.mq.MqIpLimitMessageListener;
import org.open.gateway.route.listener.mq.MqRouteDefinitionMessageListener;
import org.open.gateway.route.listener.redis.RedisRouteDefinitionMessageListener;
import org.open.gateway.route.listener.redis.topic.KeyspaceTopic;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

/**
 * Created by miko on 2020/7/15.
 * 消息监听器配置
 *
 * @author MIKO
 */
@Configuration
public class MessageListenerConfig {

    @Configuration
    @ConditionalOnProperty(prefix = "message.listener", value = "type", havingValue = "mq",
            matchIfMissing = true)
    public static class MqMessageListenerConfig {

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

    @Configuration
    @ConditionalOnProperty(prefix = "message.listener", value = "type", havingValue = "redis",
            matchIfMissing = false)
    @AutoConfigureAfter(GatewayRouteConfig.class)
    public static class RedisMessageListenerConfig {

        @Autowired
        public void subscribe(ReactiveStringRedisTemplate redisTemplate,
                              RefreshableRouteDefinitionRepository routeDefinitionRepository) {
            // 刷新路由配置
            listenRouteRefresh(redisTemplate, routeDefinitionRepository);
        }

        private void listenRouteRefresh(ReactiveStringRedisTemplate redisTemplate, RefreshableRouteDefinitionRepository repository) {
            // 监听路由配置key值变化
            RedisRouteDefinitionMessageListener messageListener = new RedisRouteDefinitionMessageListener(repository);
            redisTemplate.listenTo(new KeyspaceTopic(GatewayConstants.RedisKey.ROUTES))
                    .flatMap(messageListener::onMessage)
                    .subscribe();
        }

    }

}
