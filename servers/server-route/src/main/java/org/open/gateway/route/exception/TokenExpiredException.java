package org.open.gateway.route.exception;

import open.gateway.common.base.exception.GatewayException;

/**
 * Created by miko on 2020/7/13.
 *
 * @author MIKO
 */
public class TokenExpiredException extends GatewayException {

    public TokenExpiredException(String msg) {
        super(msg);
    }

}
