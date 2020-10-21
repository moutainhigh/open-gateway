package org.open.gateway.portal.security;

import org.open.gateway.portal.modules.account.srevice.bo.BaseAccountBO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
public class RedisTokenAuthentication implements Authentication {

    private boolean authenticated;
    private final BaseAccountBO principal;

    public RedisTokenAuthentication(BaseAccountBO principal) {
        this.principal = Objects.requireNonNull(principal);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.principal.getAccount();
    }

}
