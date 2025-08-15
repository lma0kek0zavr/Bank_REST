package com.example.Bank_REST.service.application;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    String authenticate(String userName, String password);
    
    UserDetails indetify(String token);

    boolean authorize(UserDetails userDetails, String role);
}
