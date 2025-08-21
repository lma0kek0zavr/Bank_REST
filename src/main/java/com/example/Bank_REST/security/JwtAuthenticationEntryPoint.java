package com.example.Bank_REST.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.Bank_REST.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A custom implementation of the {@link AuthenticationEntryPoint} interface, 
 * responsible for handling authentication exceptions and returning unauthorized responses.
 * 
 * This class is used by the Spring Security framework to handle situations where a user is not authenticated, 
 * and returns a JSON response with a 401 Unauthorized status code.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        
        ExceptionResponse errorResponse = new ExceptionResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                authException.getMessage(),
                request.getServletPath()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorResponse);
    }
    
}
