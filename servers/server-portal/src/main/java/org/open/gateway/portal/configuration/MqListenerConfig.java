package org.open.gateway.portal.configuration;

import org.open.gateway.portal.mq.AccessLogListener;
import org.open.gateway.portal.service.GatewayAccessLogsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by miko on 2020/7/20.
 *
 * @author MIKO
 */
@Configuration
public class MqListenerConfig {

    @Bean
    AccessLogListener accessLogListener(GatewayAccessLogsService accessLogsService) {
        return new AccessLogListener(accessLogsService);
    }

}
