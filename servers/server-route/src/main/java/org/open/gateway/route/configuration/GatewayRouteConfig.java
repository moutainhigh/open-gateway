package org.open.gateway.route.configuration;

import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.repositories.impl.JdbcClientResourcesRepository;
import org.open.gateway.route.repositories.impl.JdbcRouteDefinitionRepository;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@AutoConfigureAfter(RedisReactiveAutoConfiguration.class)
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
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
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
