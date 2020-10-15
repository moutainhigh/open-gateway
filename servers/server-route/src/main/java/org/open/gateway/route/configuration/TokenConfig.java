package org.open.gateway.route.configuration;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.security.converter.impl.RedisTokenAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

/**
 * Created by miko on 2020/6/9.
 * token配置
 *
 * @author miko
 **/
@Slf4j
@Configuration
public class TokenConfig {

    /**
     * token转换器
     *
     * @return token转换器
     */
    @Bean
    public ServerAuthenticationConverter bearerTokenConverter(ReactiveStringRedisTemplate redisTemplate) {
        return new RedisTokenAuthenticationConverter(redisTemplate);
    }

}
