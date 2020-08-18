package org.open.gateway.route.security.token.converters.redis;

import lombok.AllArgsConstructor;
import open.gateway.common.base.constants.GatewayConstants;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.security.token.AuthenticationToken;
import org.open.gateway.route.security.token.converters.AbstractBearerTokenAuthenticationConverter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
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

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    @Override
    protected Mono<Authentication> parseToken(String token) {
        return redisTemplate.opsForValue()
                .get(GatewayConstants.RedisKey.PREFIX_ACCESS_TOKENS + token)
                .cast(TokenUser.class)
                .map(AuthenticationToken::new);
    }

}
