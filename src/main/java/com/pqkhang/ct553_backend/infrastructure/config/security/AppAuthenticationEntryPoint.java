package com.pqkhang.ct553_backend.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper;

    public AppAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        delegate.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        res.setMessage(authException.getMessage());

        mapper.writeValue(response.getWriter(), res);
    }

}