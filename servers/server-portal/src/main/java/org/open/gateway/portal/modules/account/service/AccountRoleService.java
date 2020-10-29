package org.open.gateway.portal.modules.account.service;

import org.open.gateway.portal.exception.account.RoleNotExistsException;
import org.open.gateway.portal.modules.account.service.bo.BaseRoleBO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Created by miko on 10/27/20.
 *
 * @author MIKO
 */
public interface AccountRoleService {

    /**
     * 根据账户查询角色列表
     *
     * @param account 账户
     * @return 角色列表
     */
    @NonNull
    List<BaseRoleBO> queryRolesByAccount(String account);

    /**
     * 新增/修改角色
     *
     * @param roleCode    角色代码
     * @param roleName    角色名称
     * @param note        备注
     * @param operator    操作人
     * @param resourceIds 资源id集合
     */
    void save(String roleCode, String roleName, String note, String operator, List<Integer> resourceIds);

    /**
     * 启用
     *
     * @param roleCode 角色代码
     * @param operator 操作人
     */
    void enable(String roleCode, String operator) throws RoleNotExistsException;

    /**
     * 禁用
     *
     * @param roleCode 角色代码
     * @param operator 操作人
     */
    void disable(String roleCode, String operator) throws RoleNotExistsException;

    /**
     * 删除
     *
     * @param roleCode 角色代码
     * @param operator 操作人
     */
    void delete(String roleCode, String operator) throws RoleNotExistsException;

}
