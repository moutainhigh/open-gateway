package org.open.gateway.route.repositories;

import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.service.bo.ClientDetails;
import org.open.gateway.route.service.bo.OauthClientToken;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 10/15/20.
 *
 * @author MIKO
 */
public interface TokenRepository {

    /**
     * 生成token
     *
     * @param clientDetails 客户端信息
     * @return access_token
     */
    Mono<AccessToken> generate(ClientDetails clientDetails);

    /**
     * 根据客户端id获取token信息(不为null)
     *
     * @param clientId 客户端id
     * @return token信息
     */
    Mono<OauthClientToken> loadNotExpiredClientTokenByClientId(String clientId);

    /**
     * 保存token
     *
     * @param clientId   客户端id
     * @param token      token值
     * @param expireTime 过期时间
     */
    Mono<Void> saveClientToken(String clientId, String token, Long expireTime);

}
