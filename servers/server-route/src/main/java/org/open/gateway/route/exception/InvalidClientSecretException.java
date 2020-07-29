package org.open.gateway.route.exception;

import open.gateway.common.base.exception.GatewayException;

/**
 * Created by miko on 2020/7/9.
 * 无效的客户端secret
 *
 * @author MIKO
 */
public class InvalidClientSecretException extends GatewayException {

    public InvalidClientSecretException(String clientSecret) {
        super("Invalid client secret: " + clientSecret);
    }
}
