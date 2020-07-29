package org.open.gateway.route.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.security.AuthenticationManager;
import org.open.gateway.route.security.AuthorizationManager;
import org.open.gateway.route.security.filter.AccessLogFilter;
import org.open.gateway.route.security.filter.PreRequestFilter;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

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

    private final ServerCodecConfigurer configurer;
    private final ServerAuthenticationConverter serverAuthenticationConverter;
    private final RefreshableRouteDefinitionRepository resourceService;
    private final RefreshableClientResourcesRepository clientResourceService;
    private final AccessLogsService accessLogService;

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
                .access(reactiveAuthorizationManager()); // 使用自定义授权管理器

        // 初始化拦截器
        http.addFilterAt(preRequestFilter(), SecurityWebFiltersOrder.FIRST);
        // 认证拦截器
        http.addFilterAt(authenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        // 日志拦截器
        http.addFilterAt(accessLogFilter(), SecurityWebFiltersOrder.LAST);
        return http.build();
    }

    private AuthenticationWebFilter authenticationFilter() {
        AuthenticationWebFilter oauth2 = new AuthenticationWebFilter(reactiveAuthenticationManager());
        oauth2.setServerAuthenticationConverter(serverAuthenticationConverter);
        return oauth2;
    }

    public AuthenticationManager reactiveAuthenticationManager() {
        return new AuthenticationManager();
    }

    public AuthorizationManager reactiveAuthorizationManager() {
        return new AuthorizationManager(resourceService, clientResourceService);
    }

    public AccessLogFilter accessLogFilter() {
        return new AccessLogFilter(accessLogService);
    }

    public PreRequestFilter preRequestFilter() {
        return new PreRequestFilter(configurer);
    }

}
