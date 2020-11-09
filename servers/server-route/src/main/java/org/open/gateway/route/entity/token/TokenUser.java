package org.open.gateway.route.entity.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by miko on 2020/7/9.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class TokenUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 权限
     */
    private Set<String> authorities;
    /**
     * 范围
     */
    private Set<String> scopes;

}
