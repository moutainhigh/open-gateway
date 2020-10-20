package org.open.gateway.route.constants;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
public interface OAuth2Constants {

    /**
     * token 前缀
     */
    String TOKEN_PREFIX = "Bearer ";

    /**
     * 从url中获取token的参数名称
     */
    String TOKEN_PARAM_NAME = "access_token";

    interface GrantType {

        // 授权码模式
        String AUTHORIZATION_CODE = "authorization_code";
        // 密码模式
        String PASSWORD = "password";
        //
        String REFRESH_TOKEN = "refresh_token";
        // 客户端模式
        String CLIENT_CREDENTIALS = "client_credentials";
        // 简化模式
        String IMPLICIT = "implicit";

    }

}
