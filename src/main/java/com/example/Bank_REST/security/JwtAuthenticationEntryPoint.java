package com.example.Bank_REST.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
            mapper.writeValueAsString(Map.of(
                "status: ", HttpServletResponse.SC_UNAUTHORIZED,
                "error: ", "Unauthorized",
                "message: ", authException.getMessage(),
                "path: ", request.getServletPath()
            ))
        );
    }
    
}
