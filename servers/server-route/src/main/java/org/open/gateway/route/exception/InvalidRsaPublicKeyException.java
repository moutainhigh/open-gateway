package org.open.gateway.route.exception;

import open.gateway.common.base.exception.GatewayException;

/**
 * Created by miko on 2020/7/10.
 * 无效的公钥异常
 *
 * @author MIKO
 */
public class InvalidRsaPublicKeyException extends GatewayException {

    public InvalidRsaPublicKeyException(String key, String msg) {
        super("Unable to parse public key:" + key + " msg:" + msg);
    }

}
