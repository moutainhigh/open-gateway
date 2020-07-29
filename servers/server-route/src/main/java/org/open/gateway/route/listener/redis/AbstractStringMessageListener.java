package org.open.gateway.route.listener.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveSubscription;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
public abstract class AbstractStringMessageListener implements ReactiveMessageListener<String, String> {

    @Override
    public Mono<Void> onMessage(ReactiveSubscription.Message<String, String> message) {
        log.info("Receive channel:{} change message:{}", message.getChannel(), message.getMessage());
        String ops = message.getMessage();
        if (supportedOperations().contains(ops)) {
            return refresh(ops);
        } else {
            log.info("Ignore operate:" + ops);
            return Mono.empty();
        }
    }

    protected abstract Mono<Void> refresh(String ops);

    protected abstract Set<String> supportedOperations();

}