package org.open.gateway.portal.exception.gateway;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 10/30/20.
 * 无效的授权类型
 *
 * @author MIKO
 */
public class AuthorizedGrantTypeInvalidException extends ServiceException {

    public AuthorizedGrantTypeInvalidException() {
        super("invalid authorized grant type");
    }

}
