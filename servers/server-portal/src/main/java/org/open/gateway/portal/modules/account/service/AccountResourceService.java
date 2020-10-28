package org.open.gateway.portal.modules.account.service;

import org.open.gateway.portal.exception.account.ResourceNotExistsException;
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
     * 查询资源列表信息
     *
     * @return 资源
     */
    List<BaseResourceBO> queryResources();

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

    /**
     * 保存/更新资源
     *
     * @param resourceCode 资源代码
     * @param resourceName 资源名称
     * @param resourceType 资源类型
     * @param parentCode   父资源代码
     * @param perms        权限
     * @param url          地址
     * @param sort         排序
     * @param note         描述
     * @param operator     操作人
     */
    void save(String resourceCode, String resourceName, String resourceType, String parentCode, String perms, String url, Integer sort, String note, String operator);

    /**
     * 删除资源
     *
     * @param resourceCode 资源代码
     * @param operator     操作人
     * @throws ResourceNotExistsException 资源不存在
     */
    void delete(String resourceCode, String operator) throws ResourceNotExistsException;

}
