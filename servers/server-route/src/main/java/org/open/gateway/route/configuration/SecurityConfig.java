package org.open.gateway.route.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.constants.Endpoints;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.security.AuthenticationEntryPoint;
import org.open.gateway.route.security.AuthenticationManager;
import org.open.gateway.route.security.AuthorizationDeniedHandler;
import org.open.gateway.route.security.AuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/1.
 * 安全配置
 *
 * @author MIKO
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    // 白名单
    private static final String[] AUTH_WHITELIST = {
            Endpoints.OAUTH_TOKEN, // 获取token
            Endpoints.OAUTH_AUTHORIZE // 获取授权码
    };

    private final ServerAuthenticationConverter serverAuthenticationConverter;
    private final RefreshableRouteDefinitionRepository resourceService;
    private final RefreshableIpLimitRepository ipLimitRepository;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange()
                .access(authorizationManager()) // 使用自定义授权管理器
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AuthorizationDeniedHandler());

        // 认证拦截器
        http.addFilterAt(authenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    private AuthenticationWebFilter authenticationFilter() {
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(authenticationManager());
        authenticationFilter.setServerAuthenticationConverter(serverAuthenticationConverter);
        authenticationFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(new AuthenticationEntryPoint()));
        return authenticationFilter;
    }

    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager();
    }

    @Bean
    public AuthorizationManager authorizationManager() {
        return new AuthorizationManager(resourceService, ipLimitRepository);
    }

    @Bean
    public ReactiveUserDetailsService gatewayUserDetailsService() {
        return username -> Mono.empty();
    }

}
