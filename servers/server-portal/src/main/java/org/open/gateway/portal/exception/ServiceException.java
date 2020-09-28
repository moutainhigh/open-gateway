package org.open.gateway.portal.exception;

/**
 * Created by miko on 9/28/20.
 *
 * @author MIKO
 */
public abstract class ServiceException extends Exception {

    public ServiceException(String msg) {
        super(msg);
    }

}
