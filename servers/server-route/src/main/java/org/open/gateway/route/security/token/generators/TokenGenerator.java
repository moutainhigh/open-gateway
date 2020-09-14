package org.open.gateway.route.security.token.generators;

import org.open.gateway.route.entity.token.TokenUser;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
public interface TokenGenerator {

    /**
     * 生成token
     *
     * @param tokenRequest  用户token请求参数
     * @param clientDetails 客户端信息
     * @return 生成的token
     */
    Mono<String> generate(long expireAt, TokenUser tokenUser);

}
