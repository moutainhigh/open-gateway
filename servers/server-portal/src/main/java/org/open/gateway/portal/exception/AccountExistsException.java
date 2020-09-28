package org.open.gateway.portal.exception;

/**
 * Created by miko on 9/28/20.
 * 帐户已经存在
 *
 * @author MIKO
 */
public class AccountExistsException extends ServiceException {

    public AccountExistsException() {
        super("account is exists");
    }
}
