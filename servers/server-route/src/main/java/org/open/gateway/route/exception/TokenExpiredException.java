package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

/**
 * Created by miko on 2020/7/13.
 *
 * @author MIKO
 */
public class TokenExpiredException extends ResponseStatusException {

    public TokenExpiredException(String msg) {
        super(HttpStatus.UNAUTHORIZED, msg);
    }

    public TokenExpiredException(Date time) {
        super(HttpStatus.UNAUTHORIZED, "Token is expired at " + time);
    }
}
