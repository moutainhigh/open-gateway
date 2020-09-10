package org.open.gateway.route.repositories;

import org.open.gateway.route.entity.GatewayClientResourceDefinition;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
public interface RefreshableClientResourcesRepository extends RefreshableRepository {

    /**
     * 根据客户端id获取所拥有的资源路径
     *
     * @param clientId 客户端id
     * @return 资源路径
     */
    Mono<GatewayClientResourceDefinition> loadResourcePathByClientId(String clientId);

}
