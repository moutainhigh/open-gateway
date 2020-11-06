package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 网关路由配置信息
 */
@Getter
@Setter
@ToString
public class GatewayRouteDefinition implements Serializable {

    private static final long serialVersionUID = -2952097064941740301L;

    /**
     * api id
     */
    private Long apiId;

    /**
     * api编号
     */
    private String apiCode;

    /**
     * api的路径
     */
    private String apiPath;

    /**
     * 路由编号
     */
    private String routeCode;

    /**
     * 路由路径
     */
    private String routePath;

    /**
     * 路由类型 1 http，2 dubbo
     */
    private Integer routeType;

    /**
     * 代理地址
     * 例：
     * http://127.0.0.1:8080
     * dubbo://127.0.0.1:20880
     */
    private String url;

    /**
     * 转发时忽略前缀的数量
     */
    private Integer stripPrefix;

    /**
     * 失败重试 1重试，0不重试
     */
    private Integer retryTimes;

    /**
     * 限流信息
     */
    private GatewayRateLimitDefinition rateLimit;

    /**
     * 是否身份认证
     */
    private boolean auth;

    /**
     * 是否公开访问
     */
    private boolean open;

}
