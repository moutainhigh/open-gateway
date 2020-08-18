package org.open.gateway.route.entity.oauth2;

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
public class OAuth2TokenResponse {

    /**
     * token值
     */
    private String access_token;
    /**
     * token类型
     */
    private String token_type;
    /**
     * 过期时间
     */
    private long expire_in;

}
