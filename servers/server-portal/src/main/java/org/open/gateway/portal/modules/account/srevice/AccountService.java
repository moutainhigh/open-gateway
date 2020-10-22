package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.exception.AccountAlreadyExistsException;
import org.open.gateway.portal.exception.AccountNotAvailableException;
import org.open.gateway.portal.exception.AccountNotExistsException;
import org.open.gateway.portal.exception.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.springframework.lang.Nullable;

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
     * 查询有效的帐户
     *
     * @param account 帐户
     * @return 帐户信息
     * @throws AccountNotExistsException    帐户不存在
     * @throws AccountNotAvailableException 帐户不可用
     */
    BaseAccountBO queryValidBaseAccountByCode(String account) throws AccountNotExistsException, AccountNotAvailableException;

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
     * @throws AccountNotAvailableException 帐户不可用
     * @throws AccountAlreadyExistsException       帐户已经存在
     */
    BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp, String operator) throws AccountNotAvailableException, AccountAlreadyExistsException;

    /**
     * 修改
     *
     * @param account       帐户
     * @param plainPassword 密码明文
     * @param phone         电话
     * @param email         邮箱
     * @param note          描述
     * @param status        帐户状态
     * @param operator      操作人
     * @throws AccountNotAvailableException 帐户不可用
     * @throws AccountNotExistsException    帐户不存在
     */
    void update(String account, String plainPassword, String phone, String email, String note, Byte status, String operator) throws AccountNotExistsException, AccountNotAvailableException;

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
