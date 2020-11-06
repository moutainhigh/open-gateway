package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 2020/7/9.
 * 无效的客户端id
 *
 * @author MIKO
 */
public class InvalidClientIdException extends ResponseStatusException {

    public InvalidClientIdException() {
        super(HttpStatus.BAD_REQUEST, "Invalid client id");
    }

}
