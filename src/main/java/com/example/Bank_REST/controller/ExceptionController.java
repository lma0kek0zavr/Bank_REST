package com.example.Bank_REST.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.exception.ExceptionResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleResponseStatusException(
        ResponseStatusException e,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                e.getStatusCode().value(),
                e.getReason(),
                e.getMessage(),
                request.getRequestURI()
            )
        );
    }
}
