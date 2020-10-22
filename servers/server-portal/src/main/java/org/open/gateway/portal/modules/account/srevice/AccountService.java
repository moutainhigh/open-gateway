package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.exception.AccountExistsException;
import org.open.gateway.portal.exception.AccountNotAvailableException;
import org.open.gateway.portal.exception.AccountNotExistsException;
import org.open.gateway.portal.exception.AccountPasswordInvalidException;
import org.open.gateway.portal.modules.account.srevice.bo.AccountResourceBO;
import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
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
    BaseAccountBO queryAccount(String account);

    /**
     * 查询有效的帐户
     *
     * @param account 帐户
     * @return 帐户信息
     * @throws AccountNotExistsException    帐户不存在
     * @throws AccountNotAvailableException 帐户不可用
     */
    BaseAccountBO queryValidAccountByCode(String account) throws AccountNotExistsException, AccountNotAvailableException;

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
     * @throws AccountExistsException       帐户已经存在
     */
    BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp, String operator) throws AccountNotAvailableException, AccountExistsException;

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
     * @param token   登录token信息
     */
    void logout(String account, String token);

    /**
     * 查询帐户资源信息
     *
     * @param account 帐户
     * @return 资源
     */
    List<AccountResourceBO> queryResourcesByAccount(String account);

}
