package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 2020/7/9.
 * 无效的客户端secret
 *
 * @author MIKO
 */
public class InvalidClientSecretException extends ResponseStatusException {

    public InvalidClientSecretException(String clientSecret) {
        super(HttpStatus.UNAUTHORIZED, "Invalid client secret: " + clientSecret);
    }
}
