package org.open.gateway.portal.exception.account;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 9/28/20.
 * 帐户不存在
 *
 * @author MIKO
 */
public class AccountNotExistsException extends ServiceException {

    public AccountNotExistsException() {
        super("account not exists");
    }
}
