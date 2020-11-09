package org.open.gateway.portal.exception.gateway;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 11/9/20.
 *
 * @author MIKO
 */
public class RouteTypeInvalidException extends ServiceException {

    public RouteTypeInvalidException() {
        super("invalid route type");
    }
}
