package org.open.gateway.route.security.converter.impl;

import lombok.AllArgsConstructor;
import open.gateway.common.base.constants.GatewayConstants;
import open.gateway.common.utils.JSON;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.security.AuthenticationToken;
import org.open.gateway.route.security.converter.AbstractBearerTokenAuthenticationConverter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/8.
 * 从redis中获取token对应的用户信息
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisTokenAuthenticationConverter extends AbstractBearerTokenAuthenticationConverter {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    protected Mono<Authentication> parseToken(String token) {
        return this.redisTemplate.opsForValue()
                .get(GatewayConstants.RedisKey.PREFIX_ACCESS_TOKENS + token)
                .cast(String.class)
                .map(this::parseJson)
                .map(AuthenticationToken::new);
    }

    private TokenUser parseJson(String json) {
        return JSON.parse(json, TokenUser.class);
    }

}
