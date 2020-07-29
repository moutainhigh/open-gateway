package open.gateway.common.base.entity.oauth2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2020/7/7.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class OAuth2TokenRequest {

    /**
     * 客户端准入标识
     */
    private String client_id;

    /**
     * 客户端秘钥
     */
    private String client_secret;

    /**
     * 授权类型
     */
    private String grant_type;

    /**
     * 授权码
     */
    private String code;

    /**
     * 资源拥有者用户名
     */
    private String username;

    /**
     * 资源拥有者密码
     */
    private String password;

    /**
     * 申请授权码时的跳转url，一定和申请授权码时用的redirect_uri一致
     */
    private String redirect_uri;

}
