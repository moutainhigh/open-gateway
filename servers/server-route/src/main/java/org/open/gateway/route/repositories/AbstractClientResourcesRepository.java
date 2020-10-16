package org.open.gateway.route.repositories;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.common.utils.CollectionUtil;
import org.open.gateway.common.utils.CollectorUtil;
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
    private final Cache<String, Set<String>> clientResources = Caffeine.newBuilder()
//            .refreshAfterWrite(5, TimeUnit.MINUTES) // 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存
            .maximumSize(10000) // 缓存的最大数量
            .build();

    @Override
    public Mono<Set<String>> loadResourcesByClientId(String clientId) {
        return Mono.justOrEmpty(this.clientResources.getIfPresent(clientId)) // 从内存中获取
                // 找不到从数据库查询
                .switchIfEmpty(
                        getClientApiRoutes(CollectionUtil.newHashSet(clientId))
                                .collect(CollectorUtil.toHashSet(GatewayClientResourceDefinition::getFullPath))
                                .doOnNext(rs -> this.clientResources.put(clientId, rs))
                                .doOnSuccess(rs -> log.info("Reload client id:{} resource count:{}", clientId, rs.size()))
                );
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        Set<String> refreshClientIds = param.getArgs();
        return getGatewayClientResourceDefinition(refreshClientIds)
                .doOnNext(map -> {
                    if (map.size() > 0) {
                        clearResources(param);
                        this.clientResources.putAll(map);
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
            this.clientResources.cleanUp();
            log.info("[Refresh client resources] clear client resource finished");
        } else {
            this.clientResources.invalidateAll(param.getArgs());
            log.info("[Refresh client resources] remove old client resource finished");
        }
    }

    /**
     * 将客户端资源按照客户端id分组
     *
     * @param refreshClientIds api编码
     * @return 分组后的客户端资源
     */
    protected Mono<Map<String, Set<String>>> getGatewayClientResourceDefinition(Set<String> refreshClientIds) {
        return getClientApiRoutes(refreshClientIds)
                .collect(HashMap::new, (map, res) -> {
                    if (map.containsKey(res.getClientId())) {
                        map.get(res.getClientId()).add(res.getFullPath());
                    } else {
                        map.put(res.getClientId(), CollectionUtil.newHashSet(res.getFullPath()));
                    }
                });
    }

    /**
     * 根据客户端id获取客户端资源
     *
     * @param clientIds 客户端id
     * @return 客户端资源
     */
    protected abstract Flux<GatewayClientResourceDefinition> getClientApiRoutes(Set<String> clientIds);

}
