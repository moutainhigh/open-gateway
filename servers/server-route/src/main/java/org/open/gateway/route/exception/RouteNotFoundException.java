package org.open.gateway.route.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by miko on 11/6/20.
 *
 * @author MIKO
 */
public class RouteNotFoundException extends ResponseStatusException {

    public RouteNotFoundException(String msg) {
        super(HttpStatus.NOT_FOUND, msg);
    }

}
