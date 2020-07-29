package org.open.gateway.route.security.token.generators;

import open.gateway.common.base.entity.oauth2.OAuth2TokenRequest;
import open.gateway.common.base.entity.token.AccessToken;
import org.open.gateway.route.security.client.ClientDetails;

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
    AccessToken generate(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails);

}
