package org.open.gateway.portal.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
public class TokenExpiredException extends AuthenticationException {

    public TokenExpiredException() {
        super("Token is expired");
    }

}
