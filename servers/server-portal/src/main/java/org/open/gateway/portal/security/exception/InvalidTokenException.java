package org.open.gateway.portal.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super("Authentication token is invalid");
    }

}
