package com.ql.ecommerce_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String errorId = UUID.randomUUID().toString();
        logger.error("Authentication error ({}): {}", errorId, authException.getMessage());
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(403)
                .success(false)
                .message(authException.getMessage())
                .data(Map.of("error", authException.getMessage(), "errorId", errorId))
                .httpStatusCode(403)
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}
