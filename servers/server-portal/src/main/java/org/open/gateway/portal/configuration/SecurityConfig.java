package org.open.gateway.portal.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.modules.account.service.TokenService;
import org.open.gateway.portal.security.RedisTokenAuthenticationConverter;
import org.open.gateway.portal.security.RedisTokenAuthenticationFilter;
import org.open.gateway.portal.security.RedisTokenAuthenticationManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

/**
 * Created by miko on 2020/7/1.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(PortalSecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // -- swagger ui
//            "/swagger-ui.html",
//                    "/swagger-ui/*",
//                    "/swagger-resources/**",
//                    "/v2/api-docs",
//                    "/v3/api-docs",
//                    "/webjars/**",

    private final PortalSecurityProperties securityProperties;

    private final TokenService tokenService;

    //安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 动态配置访问权限
        dynamicConfigureAccess(http);
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private void dynamicConfigureAccess(HttpSecurity security) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config = security.authorizeRequests();
        Map<PortalSecurityProperties.AccessEnum, String[]> access = securityProperties.getAccess();
        if (access != null && !access.isEmpty()) {
            for (Map.Entry<PortalSecurityProperties.AccessEnum, String[]> entry : access.entrySet()) {
                config.antMatchers(entry.getValue()).access(entry.getKey().getValue());
            }

        }
        config.anyRequest().authenticated();
    }

    private RedisTokenAuthenticationFilter authenticationFilter() {
        return new RedisTokenAuthenticationFilter(new RedisTokenAuthenticationManager(), new RedisTokenAuthenticationConverter(this.tokenService));
    }

}
