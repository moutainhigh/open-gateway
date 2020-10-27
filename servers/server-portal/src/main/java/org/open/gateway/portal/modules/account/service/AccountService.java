package org.open.gateway.portal.modules.account.service;

import org.open.gateway.portal.exception.account.AccountAlreadyExistsException;
import org.open.gateway.portal.exception.account.AccountNotAvailableException;
import org.open.gateway.portal.exception.account.AccountNotExistsException;
import org.open.gateway.portal.exception.account.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountBO;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountQuery;
import org.open.gateway.portal.security.AccountDetails;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
public interface AccountService {

    /**
     * 查询帐户
     *
     * @param account 帐户
     * @return 帐户信息
     */
    @Nullable
    BaseAccountBO queryBaseAccount(String account);

    /**
     * 查询存在的账户
     *
     * @param account 账户
     * @return 账户信息
     * @throws AccountNotExistsException 账户不存在
     */
    @NonNull
    BaseAccountBO queryExistsBaseAccount(String account) throws AccountNotExistsException;

    /**
     * 查询有效的帐户
     *
     * @param account 帐户
     * @return 帐户信息
     * @throws AccountNotExistsException    帐户不存在
     * @throws AccountNotAvailableException 帐户不可用
     */
    @NonNull
    BaseAccountBO queryValidBaseAccount(String account) throws AccountNotExistsException, AccountNotAvailableException;

    /**
     * 查询账户以及权限
     *
     * @param account 账户
     * @return 账户信息
     * @throws AccountNotExistsException    帐户不存在
     * @throws AccountNotAvailableException 帐户不可用
     */
    @NonNull
    AccountDetails queryAccountDetails(String account) throws AccountNotExistsException, AccountNotAvailableException;

    /**
     * 查询账户列表
     *
     * @param query 查询条件
     * @return 账户列表
     */
    @NonNull
    List<BaseAccountBO> queryBaseAccounts(BaseAccountQuery query);

    /**
     * 注册
     *
     * @param account       帐户
     * @param plainPassword 密码明文
     * @param phone         电话
     * @param email         邮箱
     * @param note          描述
     * @param registerIp    ip地址
     * @param operator      操作人
     * @return 用户信息
     * @throws AccountAlreadyExistsException 帐户已经存在
     */
    @NonNull
    BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp, String operator) throws AccountAlreadyExistsException;

    /**
     * 修改
     *
     * @param account       帐户
     * @param plainPassword 密码明文
     * @param phone         电话
     * @param email         邮箱
     * @param note          描述
     * @param operator      操作人
     * @param roleIds       角色id集合
     * @throws AccountNotAvailableException 帐户不可用
     * @throws AccountNotExistsException    帐户不存在
     */
    void update(String account, String plainPassword, String phone, String email, String note, String operator, List<Integer> roleIds) throws AccountNotExistsException, AccountNotAvailableException;

    /**
     * 启用
     *
     * @param account  账户
     * @param operator 操作人
     * @throws AccountNotExistsException 帐户不存在
     */
    void enable(String account, String operator) throws AccountNotExistsException;

    /**
     * 禁用
     *
     * @param account  账户
     * @param operator 操作人
     * @throws AccountNotExistsException 帐户不存在
     */
    void disable(String account, String operator) throws AccountNotExistsException;

    /**
     * 登录
     *
     * @param account       帐户
     * @param plainPassword 密码明文
     * @return 登录token
     * @throws AccountPasswordInvalidException 帐户密码错误
     * @throws AccountNotExistsException       帐户不存在
     * @throws AccountNotAvailableException    帐户不可用
     */
    String login(String account, String plainPassword) throws AccountPasswordInvalidException, AccountNotExistsException, AccountNotAvailableException;

    /**
     * 登出
     *
     * @param account 登录账号
     */
    void logout(String account);

    /**
     * 删除账户
     *
     * @param account  账户
     * @param operator 操作人
     */
    void delete(String account, String operator) throws AccountNotExistsException;

}
