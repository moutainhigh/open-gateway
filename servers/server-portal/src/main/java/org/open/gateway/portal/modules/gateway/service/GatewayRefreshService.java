package org.open.gateway.portal.modules.gateway.service;

import java.util.Set;

/**
 * Created by miko on 2020/7/16.
 *
 * @author MIKO
 */
public interface GatewayRefreshService {

    /**
     * 刷新路由
     *
     * @param apiCodes api代码
     */
    void refreshRoutes(Set<String> apiCodes);

    /**
     * 刷新客户端资源关系
     *
     * @param clientIds 客户端id
     */
    void refreshClientToken(Set<String> clientIds);

    /**
     * 刷新黑白名单
     *
     * @param ipLimits ip限制id
     */
    void refreshIpLimits(Set<String> ipLimits);

}
