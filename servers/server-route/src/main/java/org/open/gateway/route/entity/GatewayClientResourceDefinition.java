package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.route.utils.PathUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miko on 2020/9/10.
 * 客户端资源定义
 *
 * @author MIKO
 */
@Getter
public class GatewayClientResourceDefinition {

    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 拥有的资源路径
     */
    private final Set<String> resources = new HashSet<>();

    public GatewayClientResourceDefinition addResource(ClientResource cr) {
        this.resources.add(PathUtil.getFullPath(cr.getRoutePath(), cr.getApiPath()));
        return this;
    }

    public GatewayClientResourceDefinition clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    @Getter
    @Setter
    @ToString
    public static class ClientResource {

        /**
         * 客户端id
         */
        private String clientId;
        /**
         * 客户端密钥
         */
        private String clientSecret;
        /**
         * 路由路径
         */
        private String routePath;
        /**
         * 接口路径
         */
        private String apiPath;

    }

}
