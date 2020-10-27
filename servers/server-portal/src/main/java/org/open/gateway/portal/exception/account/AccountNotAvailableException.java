package org.open.gateway.portal.exception.account;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 9/28/20.
 * 帐户不可用
 *
 * @author MIKO
 */
public class AccountNotAvailableException extends ServiceException {

    public AccountNotAvailableException() {
        super("account not available");
    }
}
