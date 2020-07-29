package org.open.gateway.route.exception;

import open.gateway.common.base.exception.GatewayException;

/**
 * Created by miko on 2020/7/9.
 * 无效的token
 *
 * @author MIKO
 */
public class InvalidTokenException extends GatewayException {

    public InvalidTokenException(String reason) {
        super("Invalid token:" + reason);
    }

    public InvalidTokenException() {
        super("Invalid token");
    }

}
