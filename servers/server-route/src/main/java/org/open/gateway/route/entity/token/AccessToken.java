package org.open.gateway.route.entity.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccessToken {

    /**
     * 客户端id
     */
    private String clientId;
    /**
     * token值
     */
    private String token;
    /**
     * 过期时间
     */
    private long expireAt;

}
