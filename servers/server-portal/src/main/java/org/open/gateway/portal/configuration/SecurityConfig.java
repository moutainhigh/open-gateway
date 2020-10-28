package org.open.gateway.portal.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.modules.account.service.TokenService;
import org.open.gateway.portal.security.RedisTokenAuthenticationConverter;
import org.open.gateway.portal.security.RedisTokenAuthenticationFilter;
import org.open.gateway.portal.security.RedisTokenAuthenticationManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            // -- portal
            Endpoints.ACCOUNT_LOGIN,
            Endpoints.ACCOUNT_REGISTER
    };

    private final TokenService tokenService;

    //安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated();

        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    RedisTokenAuthenticationFilter authenticationFilter() {
        return new RedisTokenAuthenticationFilter(new RedisTokenAuthenticationManager(), new RedisTokenAuthenticationConverter(this.tokenService));
    }

}
