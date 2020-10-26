package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.security.AccountDetails;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Created by miko on 10/21/20.
 *
 * @author MIKO
 */
public interface TokenService {

    /**
     * 生成并储存token
     *
     * @param accountDetails 帐户信息
     * @return token字符串
     */
    @NonNull
    String generateToken(AccountDetails accountDetails);

    /**
     * 更新token
     *
     * @param accountDetails 账户信息
     */
    void updateToken(AccountDetails accountDetails);

    /**
     * 删除token
     *
     * @param account 帐户
     * @return 删除结果
     */
    @NonNull
    Boolean deleteToken(String account);

    /**
     * 载入token用户信息
     *
     * @param token token字符串
     * @return token用户信息
     */
    @Nullable
    AccountDetails loadTokenUser(String token);

}
