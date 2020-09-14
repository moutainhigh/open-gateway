package org.open.gateway.route.security.token.generators.impl;

import lombok.AllArgsConstructor;
import open.gateway.common.base.constants.GatewayConstants;
import open.gateway.common.utils.IdUtil;
import open.gateway.common.utils.JSON;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.security.token.generators.TokenGenerator;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Created by miko on 2020/7/22.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisTokenGenerator implements TokenGenerator {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<String> generate(long expireAt, TokenUser tokenUser) {
        // 生成授权访问token
        String token = IdUtil.uuid();
        // 生成token放入redis中
        return this.redisTemplate.opsForValue()
                .set(GatewayConstants.RedisKey.PREFIX_ACCESS_TOKENS + token, JSON.toJSONString(tokenUser), Duration.ofMillis(expireAt).minusMillis(System.currentTimeMillis()))
                .thenReturn(token);
    }

}
