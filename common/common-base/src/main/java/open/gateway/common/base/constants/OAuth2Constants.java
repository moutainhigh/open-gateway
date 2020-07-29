package open.gateway.common.base.constants;

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

    interface TokenPayloadKey {

        String AUTHORITIES = "authorities";

        String SCOPE = "scope";

        String REDIRECT_URI = "redirect_uri";

        String AUTO_APPROVE = "auto_approve";

    }

}
