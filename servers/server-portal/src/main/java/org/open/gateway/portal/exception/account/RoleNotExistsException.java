package org.open.gateway.portal.exception.account;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 10/27/20.
 * 角色不存在
 *
 * @author MIKO
 */
public class RoleNotExistsException extends ServiceException {

    public RoleNotExistsException() {
        super("role not exists");
    }

}
