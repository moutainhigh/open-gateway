package org.open.gateway.portal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
public class RedisTokenAuthentication implements Authentication {

    private boolean authenticated;
    private final AccountDetails principal;
    private final Set<GrantedAuthority> authorities;

    public RedisTokenAuthentication(AccountDetails principal) {
        this.principal = Objects.requireNonNull(principal);
        this.authorities = principal.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.principal.getPassword();
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
