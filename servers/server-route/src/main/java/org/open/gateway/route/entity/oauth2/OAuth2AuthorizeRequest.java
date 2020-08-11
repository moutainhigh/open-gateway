package org.open.gateway.route.entity.oauth2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2020/7/7.
 * 认证请求
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class OAuth2AuthorizeRequest {

    /**
     * 客户端准入标识
     */
    private String client_id;

    /**
     * 授权码模式固定为code
     */
    private String response_type;

    /**
     * 客户端权限
     */
    private String scope;

    /**
     * 申请授权码时的跳转url，一定和申请授权码时用的redirect_uri一致
     */
    private String redirect_uri;

}
