package org.open.gateway.portal.modules.account.srevice;

import org.open.gateway.portal.modules.account.srevice.bo.AccountResourceBO;

/**
 * Created by miko on 9/27/20.
 *
 * @author MIKO
 */
public interface ResourceService {

    AccountResourceBO queryResourcesByAccount(String account);

}
