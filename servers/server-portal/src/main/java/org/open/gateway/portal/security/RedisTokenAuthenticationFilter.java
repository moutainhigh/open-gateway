package org.open.gateway.portal.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class RedisTokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisTokenAuthenticationManager authenticationManager;
    private final RedisTokenAuthenticationConverter authenticationConverter;
    private final AuthenticationFailureHandler failureHandler = new AuthenticationEntryPointFailureHandler(new AuthenticationFailureEntryPoint(HttpStatus.UNAUTHORIZED));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Authentication authentication = authenticationConverter.convert(request);
            Authentication authenticationResult = authenticationManager.authenticate(authentication);
            successfulAuthentication(authenticationResult);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
        }
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private void successfulAuthentication(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

}
