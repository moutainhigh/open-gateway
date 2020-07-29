package org.open.gateway.portal.service;

import java.util.Set;

/**
 * Created by miko on 2020/7/16.
 *
 * @author MIKO
 */
public interface GatewayService {

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
    void refreshResources(Set<String> clientIds);

}
