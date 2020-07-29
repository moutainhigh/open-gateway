package org.open.gateway.route.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/8.
 * 认证管理器
 *
 * @author MIKO
 */
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        // 直接认证通过
        authentication.setAuthenticated(authentication.getPrincipal() != null);
        return Mono.just(authentication);
    }

}
