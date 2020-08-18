package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ClientResource {

    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 路由路径
     */
    private String routePath;
    /**
     * 接口路径
     */
    private String apiPath;

}
