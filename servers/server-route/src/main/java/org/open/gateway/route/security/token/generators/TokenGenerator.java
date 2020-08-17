package org.open.gateway.route.security.token.generators;

import org.open.gateway.route.entity.oauth2.OAuth2TokenRequest;
import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.service.bo.ClientDetails;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
public interface TokenGenerator {

    /**
     * 支持的授权类型
     *
     * @param grantType 授权类型
     * @return true支持，false不支持
     */
    boolean isSupported(String grantType);

    /**
     * 生成token
     *
     * @param tokenRequest  用户token请求参数
     * @param clientDetails 客户端信息
     * @return 生成的token
     */
    Mono<AccessToken> generate(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails);

}
