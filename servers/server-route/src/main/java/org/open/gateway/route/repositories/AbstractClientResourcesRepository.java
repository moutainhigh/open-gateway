package org.open.gateway.route.repositories;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.collection.LRUCache;
import open.gateway.common.base.entity.RefreshGateway;
import open.gateway.common.utils.CollectionUtil;
import org.open.gateway.route.entity.GatewayClientResourceDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private final Map<String, GatewayClientResourceDefinition> clientResources = new LRUCache<>(10000);

    @Override
    public Mono<GatewayClientResourceDefinition> loadResourcePathByClientId(String clientId) {
        return Mono.justOrEmpty(this.clientResources.get(clientId)) // 从内存中获取
                // 找不到从数据库查询
                .switchIfEmpty(
                        getClientApiRoutes(CollectionUtil.newHashSet(clientId))
                                .reduce(new GatewayClientResourceDefinition(), (crd, cr) -> crd.addResource(cr).clientSecret(cr.getClientSecret()))
                                .doOnNext(rs -> {
                                    synchronized (this.clientResources) {
                                        this.clientResources.put(clientId, rs);
                                    }
                                })
                                .doOnSuccess(rs -> log.info("Reload client id:{} resource count:{}", clientId, rs.getResources().size()))
                );
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        Set<String> refreshClientIds = param.getArgs();
        return getGatewayClientResourceDefinition(refreshClientIds)
                .doOnNext(map -> {
                    if (map.size() > 0) {
                        synchronized (this.clientResources) {
                            clearResources(param);
                            this.clientResources.putAll(map);
                        }
                    }
                })
                .doOnSubscribe(v -> log.info("[Refresh client resources] starting. target client ids:{}", refreshClientIds))
                .doOnSuccess(map -> log.info("[Refresh client resources] finished"))
                .doOnError(e -> log.error("[Refresh client resources] failed reason:{}", e.getMessage()))
                .then();
    }

    /**
     * 清理客户端资源
     *
     * @param param api编码
     */
    private void clearResources(RefreshGateway param) {
        if (param.isRefreshAll()) {
            this.clientResources.clear();
            log.info("[Refresh client resources] clear client resource finished");
        } else {
            param.getArgs().forEach(this.clientResources::remove);
            log.info("[Refresh client resources] remove old client resource finished");
        }
    }

    /**
     * 将客户端资源按照客户端id分组
     *
     * @param refreshClientIds api编码
     * @return 分组后的客户端资源
     */
    protected Mono<Map<String, GatewayClientResourceDefinition>> getGatewayClientResourceDefinition(Set<String> refreshClientIds) {
        return getClientApiRoutes(refreshClientIds)
                .collect(HashMap::new, (map, res) -> {
                    if (map.containsKey(res.getClientId())) {
                        map.get(res.getClientId())
                                .addResource(res);
                    } else {
                        map.put(res.getClientId(), new GatewayClientResourceDefinition()
                                .clientSecret(res.getClientSecret())
                                .addResource(res)
                        );
                    }
                });
    }

    /**
     * 根据客户端id获取客户端资源
     *
     * @param clientIds 客户端id
     * @return 客户端资源
     */
    protected abstract Flux<GatewayClientResourceDefinition.ClientResource> getClientApiRoutes(Set<String> clientIds);

}
