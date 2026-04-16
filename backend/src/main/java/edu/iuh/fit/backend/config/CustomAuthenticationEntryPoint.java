/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.iuh.fit.backend.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/json; charset=UTF-8");

        var res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        String errorMessage = Optional.ofNullable(authException.getCause())
                                .map(Throwable::getMessage)
                                .orElse(authException.getMessage());
        res.setErrorCode(errorMessage);
        res.setMessage("Invalid Token");
        objectMapper.writeValue(response.getWriter(), res);
    }
}
