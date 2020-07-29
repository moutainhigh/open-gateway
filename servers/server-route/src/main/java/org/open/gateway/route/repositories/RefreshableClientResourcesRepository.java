package org.open.gateway.route.repositories;

import open.gateway.common.base.entity.RefreshGateway;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
public interface RefreshableClientResourcesRepository extends RefreshableRepository<RefreshGateway> {

    /**
     * 根据客户端id获取所拥有的资源路径
     *
     * @param clientId 客户端id
     * @return 资源路径
     */
    Mono<Set<String>> loadResourcePathByClientId(String clientId);

}
