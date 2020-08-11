package org.open.gateway.route.repositories;

import open.gateway.common.base.entity.RefreshGateway;
import org.open.gateway.route.entity.GatewayIpLimit;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/8/3.
 *
 * @author MIKO
 */
public interface RefreshableIpLimitRepository extends RefreshableRepository<RefreshGateway> {

    /**
     * 加载ip限制策略
     *
     * @param apiCode api代码
     * @return ip限制信息
     */
    Mono<GatewayIpLimit> loadIpLimitByApi(String apiCode);

}
