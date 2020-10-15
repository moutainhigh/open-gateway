package org.open.gateway.route.configuration;

import open.gateway.common.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.repositories.r2dbc.R2dbcClientResourcesRepository;
import org.open.gateway.route.repositories.r2dbc.R2dbcIpLimitRepository;
import org.open.gateway.route.repositories.r2dbc.R2dbcRouteDefinitionRepository;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.beans.BeansException;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * Created by miko on 2020/6/9.
 * 网关路由配置
 *
 * @author MIKO
 */
@Configuration
public class GatewayRouteConfig implements ApplicationContextAware {

    @Bean
    public RefreshableRouteDefinitionRepository routeDefinitionRepository(DatabaseClient databaseClient) {
        return new R2dbcRouteDefinitionRepository(databaseClient, 60 * 60);
    }

    @Bean
    public RefreshableClientResourcesRepository clientResourcesRepository(DatabaseClient databaseClient) {
        return new R2dbcClientResourcesRepository(databaseClient, 60 * 60);
    }

    @Bean
    public RefreshableIpLimitRepository ipLimitRepository(DatabaseClient databaseClient) {
        return new R2dbcIpLimitRepository(databaseClient, 60 * 60);
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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, RefreshableRepository> repositoryMap = applicationContext.getBeansOfType(RefreshableRepository.class);
        repositoryMap.values().forEach(this::refreshInterval);
    }

    private void refreshInterval(RefreshableRepository repository) {
        // 初始化刷新1次, 之后每一定时间刷新一次
        Flux.interval(Duration.ofSeconds(repository.delay()), Duration.ofSeconds(repository.refreshInterval()))
                .flatMap(count -> repository.refresh(new RefreshGateway()))
                .subscribe();
    }

}
