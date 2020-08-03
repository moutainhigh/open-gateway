package org.open.gateway.route.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.security.AuthenticationManager;
import org.open.gateway.route.security.AuthorizationManager;
import org.open.gateway.route.security.filter.AuthenticationEntryPoint;
import org.open.gateway.route.security.filter.AuthorizationDeniedHandler;
import org.open.gateway.route.security.filter.PreRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;

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

    private final ServerAuthenticationConverter serverAuthenticationConverter;
    private final RefreshableRouteDefinitionRepository resourceService;
    private final RefreshableClientResourcesRepository clientResourceService;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange()
                .pathMatchers("/", "/oauth/*").permitAll()
                .anyExchange()
                .access(reactiveAuthorizationManager()) // 使用自定义授权管理器
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AuthorizationDeniedHandler());

        http.addFilterAt(new PreRequestFilter(), SecurityWebFiltersOrder.FIRST);
        // 认证拦截器
        http.addFilterAt(authenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    private AuthenticationWebFilter authenticationFilter() {
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authenticationFilter.setServerAuthenticationConverter(serverAuthenticationConverter);
        authenticationFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(new AuthenticationEntryPoint()));
        return authenticationFilter;
    }

    public AuthenticationManager reactiveAuthenticationManager() {
        return new AuthenticationManager();
    }

    public AuthorizationManager reactiveAuthorizationManager() {
        return new AuthorizationManager(resourceService, clientResourceService);
    }

}
