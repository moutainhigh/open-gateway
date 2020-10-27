package org.open.gateway.portal.modules.account.service;

import org.open.gateway.portal.modules.account.service.bo.BaseResourceBO;

import java.util.List;
import java.util.Set;

/**
 * Created by miko on 10/22/20.
 *
 * @author MIKO
 */
public interface AccountResourceService {

    /**
     * 根据账户查询资源信息
     *
     * @param account 帐户
     * @return 资源
     */
    List<BaseResourceBO> queryResourcesByAccount(String account);

    /**
     * 根据角色查询资源信息
     *
     * @param roleCode 角色代码
     * @return 资源
     */
    List<BaseResourceBO> queryResourcesByRole(String roleCode);

    /**
     * 根据账户查询权限信息
     *
     * @param account 账户
     * @return 权限
     */
    Set<String> queryPermsByAccount(String account);

}
