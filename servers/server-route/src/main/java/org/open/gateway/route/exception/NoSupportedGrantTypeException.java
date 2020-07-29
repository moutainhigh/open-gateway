package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 2020/7/9.
 * 不支持的授权类型
 *
 * @author MIKO
 */
public class NoSupportedGrantTypeException extends ResponseStatusException {

    public NoSupportedGrantTypeException(String grantType) {
        super(HttpStatus.UNAUTHORIZED, "No supported grantType: " + grantType);
    }
}
