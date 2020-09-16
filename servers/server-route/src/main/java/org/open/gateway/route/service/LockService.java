package org.open.gateway.route.service;

import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/9/16.
 *
 * @author MIKO
 */
public interface LockService {

    /**
     * 加锁
     *
     * @param key            加锁的key
     * @param doOnLocked     加锁成功执行的操作
     * @param doOnLockFailed 加锁失败执行的操作
     */
    <T> Mono<T> lock(String key, Mono<T> doOnLocked, Mono<T> doOnLockFailed);

}
