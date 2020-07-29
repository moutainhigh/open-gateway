package org.open.gateway.route.exception;

import open.gateway.common.base.exception.GatewayException;

/**
 * Created by miko on 2020/7/9.
 * 未找到客户端id
 *
 * @author MIKO
 */
public class NoClientFoundException extends GatewayException {

    public NoClientFoundException() {
        super("No client found");
    }

}
