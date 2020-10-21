package org.open.gateway.portal.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
public class RedisTokenAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.setAuthenticated(authentication instanceof RedisTokenAuthentication);
        return authentication;
    }

}
