package org.open.gateway.route.listener.redis;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.entity.RefreshGateway;
import open.gateway.common.utils.CollectionUtil;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
public class RedisRouteDefinitionMessageListener extends AbstractStringMessageListener {

    private final RefreshableRouteDefinitionRepository repository;

    public RedisRouteDefinitionMessageListener(RefreshableRouteDefinitionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Mono<Void> refresh(String ops) {
        return this.repository.refresh(new RefreshGateway());
    }

    @Override
    protected Set<String> supportedOperations() {
        return CollectionUtil.newHashSet("set", "del", "hset", "hdel");
    }
}