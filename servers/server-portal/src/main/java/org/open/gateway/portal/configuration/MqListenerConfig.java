package org.open.gateway.portal.configuration;

import org.open.gateway.portal.mq.AccessLogListener;
import org.open.gateway.portal.service.GatewayAccessLogsService;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by miko on 2020/7/20.
 *
 * @author MIKO
 */
@Configuration
public class MqListenerConfig {

    @Bean("batchQueueRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory batchQueueRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置批量
        factory.setBatchListener(true);
        //设置BatchMessageListener生效
        factory.setConsumerBatchEnabled(true);
        //设置监听器一次批量处理的消息数量
        factory.setBatchSize(500);
        return factory;
    }

    @Bean
    AccessLogListener accessLogListener(GatewayAccessLogsService accessLogsService) {
        return new AccessLogListener(accessLogsService);
    }

}
