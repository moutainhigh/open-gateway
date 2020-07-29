package org.open.gateway.route.listener.redis;

import org.springframework.data.redis.connection.ReactiveSubscription;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/6/11.
 *
 * @author MIKO
 */
public interface ReactiveMessageListener<C, M> {

    Mono<Void> onMessage(ReactiveSubscription.Message<C, M> message);

}
