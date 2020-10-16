package org.open.gateway.route.repositories;

import org.open.gateway.base.entity.RefreshGateway;
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

}
