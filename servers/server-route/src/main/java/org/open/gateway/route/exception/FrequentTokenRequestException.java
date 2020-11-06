package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 2020/9/15.
 * token请求太频繁
 *
 * @author MIKO
 */
public class FrequentTokenRequestException extends ResponseStatusException {

    public FrequentTokenRequestException() {
        super(HttpStatus.TOO_MANY_REQUESTS, "It is not allowed to apply for token at the same time.");
    }
}
