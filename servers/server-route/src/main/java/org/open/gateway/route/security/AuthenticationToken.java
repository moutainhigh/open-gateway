package org.open.gateway.route.security;

import org.open.gateway.route.entity.token.TokenUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/8.
 *
 * @author MIKO
 */
public class AuthenticationToken implements Authentication {

    /**
     * 是否认证过
     */
    private boolean authenticated = false;
    /**
     * 用户信息
     */
    private final TokenUser principal;

    public AuthenticationToken(TokenUser tokenUser) {
        this.principal = Objects.requireNonNull(tokenUser, "token user is required.");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.principal.getAuthorities() == null ? null : this.principal.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.principal.getClientId();
    }
}
