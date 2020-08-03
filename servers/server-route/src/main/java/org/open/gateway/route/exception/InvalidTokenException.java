package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 2020/7/9.
 * 无效的token
 *
 * @author MIKO
 */
public class InvalidTokenException extends ResponseStatusException {

    public InvalidTokenException(String reason) {
        super(HttpStatus.UNAUTHORIZED, "Invalid token:" + reason);
    }

    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid token");
    }

}
