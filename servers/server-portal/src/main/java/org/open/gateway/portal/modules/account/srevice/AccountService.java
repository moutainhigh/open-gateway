package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;

import javax.validation.constraints.NotNull;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
public interface AccountService {

    @NotNull
    BaseAccountBO queryBaseAccountByCode(String account);

    /**
     * 注册
     *
     * @param account       帐户
     * @param plainPassword 密码明文
     * @param phone         电话
     * @param email         邮箱
     * @param note          描述
     * @param registerIp    ip地址
     * @return 用户信息
     */
    BaseAccountBO register(String account, String plainPassword, String phone, String email, String note, String registerIp);

    /**
     * 登录
     *
     * @param account       帐户
     * @param plainPassword 密码明文
     * @return 登录token
     */
    String login(String account, String plainPassword);

}
