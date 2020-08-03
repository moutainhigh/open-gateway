package org.open.gateway.route.repositories;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.collection.LRUCache;
import open.gateway.common.base.entity.ClientResource;
import open.gateway.common.base.entity.RefreshGateway;
import open.gateway.common.utils.CollectionUtil;
import org.open.gateway.route.utils.PathUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
@Slf4j
public abstract class AbstractClientResourcesRepository implements RefreshableClientResourcesRepository {

    /**
     * 客户端资源分组。 key为客户端id，value为该客户端所拥有的资源
     */
    private final Map<String, Set<String>> clientResources = synchronizedMap(new LRUCache<>(10000));

    @Override
    public Mono<Set<String>> loadResourcePathByClientId(String clientId) {
        return Mono.justOrEmpty(clientResources.get(clientId)) // 从内存中获取
                // 找不到从数据库查询
                .switchIfEmpty(
                        getClientApiRoutes(CollectionUtil.newHashSet(clientId))
                                .map(cr -> PathUtil.getFullPath(cr.getRoutePath(), cr.getApiPath()))
                                .collect(Collectors.toSet())
                                .doOnSuccess(rs -> clientResources.put(clientId, rs))
                );
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        Set<String> clientIds = param == null ? null : param.getArgs();
        Mono<Void> cleanResources = Mono.fromRunnable(() -> this.clearResources(clientIds));
        return cleanResources.then(
                groupClientResource(getClientApiRoutes(clientIds))
                        .doOnSuccess(map -> {
                            this.clientResources.putAll(map);
                            log.info("Refresh client resources finished");
                        })
                        .doOnError(e -> log.error("Refresh client resources failed reason:{}", e.getMessage()))
                        .then()
        );
    }

    /**
     * 清理客户端资源
     *
     * @param clientIds 客户端id
     */
    protected void clearResources(Set<String> clientIds) {
        if (clientIds != null) {
            clientIds.forEach(this.clientResources::remove);
            log.info("Clear client resource finished. client ids:" + clientIds);
        } else {
            this.clientResources.clear();
            log.info("Clear all client resource finished");
        }
    }

    /**
     * 将客户端资源按照客户端id分组
     *
     * @param clientResources 客户端资源
     * @return 分组后的客户端资源
     */
    protected Mono<Map<String, Set<String>>> groupClientResource(Flux<ClientResource> clientResources) {
        return clientResources
                .collect(HashMap::new, (map, api) -> {
                    String path = PathUtil.getFullPath(api.getRoutePath(), api.getApiPath());
                    if (map.containsKey(api.getClientId())) {
                        map.get(api.getClientId()).add(path);
                    } else {
                        map.put(api.getClientId(), CollectionUtil.newHashSet(path));
                    }
                });
    }

    /**
     * 根据客户端id获取客户端资源
     *
     * @param clientIds 客户端id
     * @return 客户端资源
     */
    protected abstract Flux<ClientResource> getClientApiRoutes(Set<String> clientIds);

}
