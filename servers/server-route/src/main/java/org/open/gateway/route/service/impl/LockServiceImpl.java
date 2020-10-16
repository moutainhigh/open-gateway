package org.open.gateway.route.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.GatewayConstants;
import org.open.gateway.route.service.LockService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Created by miko on 2020/9/16.
 *
 * @author MIKO
 */
@Slf4j
@Service
@AllArgsConstructor
public class LockServiceImpl implements LockService {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public <T> Mono<T> lock(String key, Mono<T> doOnLocked, Mono<T> doOnLockFailed) {
        // 加锁key
        String lockKey = GatewayConstants.RedisKey.PREFIX_LOCK + key;
        return redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "", Duration.ofSeconds(10)) // 加锁, 10s后过期(防止解锁异常死锁)
                .flatMap(b -> {
                    log.info("[Redis Lock] lock key:{} result:{}", lockKey, b);
                    if (!b) {
                        // 加锁失败 返回失败映射
                        return doOnLockFailed;
                    }
                    // 加锁成功
                    return doOnLocked.doOnTerminate(
                            () -> redisTemplate.opsForValue()
                                    .delete(lockKey).doOnSuccess(o -> log.info("[Redis Lock] unlock lock key:{} result:{}", lockKey, o))
                                    .subscribe()
                    ); // 删除redis key(解锁)
                });
    }

}
