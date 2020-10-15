package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import org.open.gateway.route.utils.UrlUtil;

/**
 * Created by miko on 2020/9/10.
 * 客户端资源定义
 *
 * @author MIKO
 */
@Getter
@Setter
public class GatewayClientResourceDefinition {

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

    public String getFullPath() {
        return UrlUtil.appendUrlPath(this.getRoutePath(), this.getApiPath());
    }

}
