package org.open.gateway.portal.modules.gateway.service;

import org.open.gateway.portal.exception.gateway.GatewayAppNotExistsException;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppBO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
public interface GatewayAppService {

    /**
     * 查询网关应用
     *
     * @param clientId 应用id
     * @param appName  应用名称
     * @return 应用集合
     */
    @NonNull
    List<GatewayAppBO> queryGatewayApps(String clientId, String appName);

    /**
     * 按照应用id查询应用
     *
     * @param clientId 应用id
     * @return 应用信息
     */
    @NonNull
    GatewayAppBO queryGatewayAppByClientId(String clientId) throws GatewayAppNotExistsException;

}
