package org.open.gateway.portal.exception.account;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 9/28/20.
 * 帐户已经存在
 *
 * @author MIKO
 */
public class AccountAlreadyExistsException extends ServiceException {

    public AccountAlreadyExistsException() {
        super("account already exists");
    }
}
