package org.open.gateway.route.security.token.generators.impl;

import lombok.AllArgsConstructor;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.security.token.generators.TokenGenerator;
import org.open.gateway.route.utils.jwt.Jwts;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/8.
 * 客户端模式token生成器
 *
 * @author MIKO
 */
@AllArgsConstructor
public class JwtTokenGenerator implements TokenGenerator {

    private final Jwts jwtEncoder;

    @Override
    public Mono<String> generate(long expireAt, TokenUser tokenUser) {
        return Mono.just(
                this.jwtEncoder.generateToken(
                        tokenUser.getClientId(),
                        expireAt,
                        tokenUser
                )
        );
    }

}
