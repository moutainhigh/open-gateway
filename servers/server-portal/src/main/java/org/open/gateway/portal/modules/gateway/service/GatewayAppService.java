package org.open.gateway.portal.modules.gateway.service;

import org.open.gateway.portal.exception.gateway.AuthorizedGrantTypeInvalidException;
import org.open.gateway.portal.exception.gateway.GatewayAppNotExistsException;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppBO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
public interface GatewayAppService {

    /**
     * 查询网关应用
     *
     * @param appCode 应用代码
     * @param appName 应用名称
     * @return 应用集合
     */
    @NonNull
    List<GatewayAppBO> queryGatewayApps(String appCode, String appName);

    /**
     * 按照应用id查询应用
     *
     * @param clientId 应用id
     * @return 应用信息
     * @throws GatewayAppNotExistsException 应用不存在
     */
    @NonNull
    GatewayAppBO queryGatewayAppByClientId(String clientId) throws GatewayAppNotExistsException;

    /**
     * 保存/更新应用
     *
     * @param appCode              应用代码
     * @param appName              应用名称
     * @param note                 备注
     * @param registerFrom         注册来源
     * @param accessTokenValidity  token持续时间
     * @param webServerRedirectUri 回调地址
     * @param authorizedGrantTypes 授权类型
     * @param operator             操作人
     * @throws AuthorizedGrantTypeInvalidException 无效的授权类型
     */
    void save(String appCode, String appName, String note, String registerFrom, int accessTokenValidity, String webServerRedirectUri, Set<String> authorizedGrantTypes, String operator) throws AuthorizedGrantTypeInvalidException;

}
