package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;

/**
 * Created by miko on 10/21/20.
 *
 * @author MIKO
 */
public interface TokenService {

    /**
     * 生成并储存token
     *
     * @param account 帐户
     * @return token字符串
     */
    String generateToken(BaseAccountBO account);

    /**
     * 删除token
     *
     * @param account 帐户
     * @return 删除结果
     */
    Boolean deleteToken(String account);

    /**
     * 载入token用户信息
     *
     * @param token token字符串
     * @return token用户信息
     */
    BaseAccountBO loadTokenUser(String token);

}
