package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountResourceBO;

import java.util.List;

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
    List<BaseAccountResourceBO> queryResourcesByAccount(String account);

}
