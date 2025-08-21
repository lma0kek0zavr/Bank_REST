package com.example.Bank_REST.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class CardOperationException extends ResponseStatusException {

    public CardOperationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
    
}
