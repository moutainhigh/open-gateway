package org.open.gateway.route.repositories;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/6/10.
 *
 * @author MIKO
 */
public interface RefreshableRouteDefinitionRepository extends RouteDefinitionRepository, RefreshableRepository {

    /**
     * 根据路径获取路由信息
     *
     * @param path 请求路径
     * @return 路由信息
     */
    Mono<RouteDefinition> loadRouteDefinition(String path);

}
