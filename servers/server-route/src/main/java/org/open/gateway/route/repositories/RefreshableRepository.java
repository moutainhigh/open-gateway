package org.open.gateway.route.repositories;

import open.gateway.common.base.entity.RefreshGateway;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
public interface RefreshableRepository {

    /**
     * 刷新资源
     */
    Mono<Void> refresh(RefreshGateway param);

    /**
     * 刷新频率 单位秒
     *
     * @return 秒
     */
    int refreshInterval();

    /**
     * 第一次延迟多少秒刷新
     */
    default int delay() {
        return 0;
    }

}
