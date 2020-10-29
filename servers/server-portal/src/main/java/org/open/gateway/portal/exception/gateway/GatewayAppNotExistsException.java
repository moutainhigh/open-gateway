package org.open.gateway.portal.exception.gateway;

import org.open.gateway.portal.exception.ServiceException;

/**
 * Created by miko on 10/29/20.
 *
 * @author MIKO
 */
public class GatewayAppNotExistsException extends ServiceException {

    public GatewayAppNotExistsException() {
        super("gateway app not exists");
    }

}
