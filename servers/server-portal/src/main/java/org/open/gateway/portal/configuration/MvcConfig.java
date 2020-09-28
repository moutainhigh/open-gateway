package org.open.gateway.portal.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import open.gateway.common.utils.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by miko on 9/28/20.
 *
 * @author MIKO
 */
@Configuration
public class MvcConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JSON.getJsonMapper();
    }

}
