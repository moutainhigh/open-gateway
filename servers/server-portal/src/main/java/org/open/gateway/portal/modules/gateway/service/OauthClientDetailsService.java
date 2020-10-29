package org.open.gateway.portal.modules.gateway.service;

import org.open.gateway.portal.modules.gateway.service.bo.OauthClientDetailsBO;
import org.springframework.lang.Nullable;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
public interface OauthClientDetailsService {

    /**
     * 根据应用id查询oauth客户端配置
     *
     * @param clientId 应用id
     * @return oauth客户端配置
     */
    @Nullable
    OauthClientDetailsBO queryClientDetailsByClientId(String clientId);

}
