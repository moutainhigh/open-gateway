package org.open.gateway.portal.exception;

/**
 * Created by miko on 9/28/20.
 * 无效的密码
 *
 * @author MIKO
 */
public class AccountPasswordInvalidException extends ServiceException {

    public AccountPasswordInvalidException() {
        super("account password invalid");
    }

}