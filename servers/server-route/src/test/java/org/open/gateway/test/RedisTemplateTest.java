package org.open.gateway.test;

import open.gateway.common.utils.StringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Created by miko on 2020/9/15.
 *
 * @author MIKO
 */
public class RedisTemplateTest extends BaseSpringTest {

    @Autowired
    ReactiveStringRedisTemplate redisTemplate;

    @Test
    public void testSetIfAbsent() {
        String key = "test_key";
        Flux.<String>generate(sink -> sink.next(key))
                .take(10000)
                .parallel(500)
                .runOn(Schedulers.parallel())
                .flatMap(k -> doSetValue(k, "value:" + StringUtil.randomLetter(4)))
                .sequential()
                .collectList()
                .doOnSuccess(result -> {
                    long count = result.stream().filter(b -> b).count();
                    System.out.println("lock success count:" + count);
                    Assert.isTrue(count == 1, "lock success count should be 1");
                })
                .then(
                        redisTemplate.opsForValue().get(key)
                                .doOnNext(b -> System.out.println("Thread:" + currentThreadName() + "get key:" + key + " result:" + b))
                )
                .block();
    }

    private Mono<Boolean> doSetValue(String key, String test_value1) {
        return redisTemplate.opsForValue().setIfAbsent(key, test_value1, Duration.ofMillis(100000))
                .doOnNext(b -> System.out.println("Thread:" + currentThreadName() + " setIfAbsent key:" + key + " result:" + b));
    }

}
