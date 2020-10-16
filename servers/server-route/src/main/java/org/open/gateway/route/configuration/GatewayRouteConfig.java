package org.open.gateway.route.configuration;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.repositories.impl.R2dbcClientResourcesRepository;
import org.open.gateway.route.repositories.impl.R2dbcIpLimitRepository;
import org.open.gateway.route.repositories.impl.R2dbcRouteDefinitionRepository;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/6/9.
 * 网关路由配置
 *
 * @author MIKO
 */
@Slf4j
@Configuration
public class GatewayRouteConfig implements BeanPostProcessor {

    @Bean
    public RefreshableRouteDefinitionRepository routeDefinitionRepository(DatabaseClient databaseClient) {
        return new R2dbcRouteDefinitionRepository(databaseClient);
    }

    @Bean
    public RefreshableClientResourcesRepository clientResourcesRepository(DatabaseClient databaseClient) {
        return new R2dbcClientResourcesRepository(databaseClient);
    }

    @Bean
    public RefreshableIpLimitRepository ipLimitRepository(DatabaseClient databaseClient) {
        return new R2dbcIpLimitRepository(databaseClient);
    }

    @Primary
    @Bean("urlUserKeyResolver")
    public KeyResolver urlUserKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value() + WebExchangeUtil.getClientId(exchange));
    }

    @Bean("urlKeyResolver")
    public KeyResolver urlKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    @Bean("userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(WebExchangeUtil.getClientId(exchange));
    }

    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(WebExchangeUtil.getRemoteAddress(exchange));
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RefreshableRepository) {
            // 初始化刷新
            ((RefreshableRepository) bean).refresh(new RefreshGateway()).subscribe();
        }
        return bean;
    }

}
