package com.example.Bank_REST.service.application.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.Bank_REST.security.JwtUtil;
import com.example.Bank_REST.service.application.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Override
    public String authenticate(String userName, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userName, password)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public UserDetails indetify(String token) {
        String userName = jwtUtil.getUserName(token);

        return userDetailsService.loadUserByUsername(userName);
    }

    @Override
    public boolean authorize(UserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream()
            .anyMatch(ga -> ga.getAuthority().equals(role));
    }

    
}
