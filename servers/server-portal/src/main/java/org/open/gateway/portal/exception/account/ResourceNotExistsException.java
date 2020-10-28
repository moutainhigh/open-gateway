package org.open.gateway.portal.exception.account;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 10/28/20.
 *
 * @author MIKO
 */
public class ResourceNotExistsException extends ServiceException {

    public ResourceNotExistsException() {
        super("resource not exists");
    }

}
