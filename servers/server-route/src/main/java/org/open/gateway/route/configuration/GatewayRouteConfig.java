package org.open.gateway.route.configuration;

import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.repositories.jdbc.JdbcClientResourcesRepository;
import org.open.gateway.route.repositories.jdbc.JdbcIpLimitRepository;
import org.open.gateway.route.repositories.jdbc.JdbcRouteDefinitionRepository;
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
        return new JdbcRouteDefinitionRepository(databaseClient);
    }

    @Bean
    public RefreshableClientResourcesRepository clientResourcesRepository(DatabaseClient databaseClient) {
        return new JdbcClientResourcesRepository(databaseClient);
    }

    @Bean
    public RefreshableIpLimitRepository ipLimitRepository(DatabaseClient databaseClient) {
        return new JdbcIpLimitRepository(databaseClient);
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

    @SuppressWarnings("rawtypes")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, RefreshableRepository> repositoryMap = applicationContext.getBeansOfType(RefreshableRepository.class);
        repositoryMap.values().forEach(this::refreshInterval);
    }

    private void refreshInterval(RefreshableRepository<?> repository) {
        // 初始化刷新1次, 之后每一定时间刷新一次
        Flux.interval(Duration.ofSeconds(repository.delay()), Duration.ofSeconds(repository.refreshInterval()))
                .flatMap(count -> repository.refresh(null))
                .subscribe();
    }

}
