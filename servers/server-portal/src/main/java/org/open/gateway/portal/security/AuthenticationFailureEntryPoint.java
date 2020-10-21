package org.open.gateway.portal.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by miko on 10/20/20.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class AuthenticationFailureEntryPoint implements AuthenticationEntryPoint {

    private final HttpStatus httpStatus;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(httpStatus.value());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(authException.getMessage());
        }
    }

}
