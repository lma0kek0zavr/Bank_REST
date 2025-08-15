package com.example.Bank_REST.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse{

    private int status;

    private String message;

    private String details;

    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    public ExceptionResponse(int status, String message, String details, String url) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.url = url;
        this.timestamp = LocalDateTime.now();
    }
}
