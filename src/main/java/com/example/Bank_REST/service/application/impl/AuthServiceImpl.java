package com.example.Bank_REST.service.application.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.Bank_REST.security.JwtUtil;
import com.example.Bank_REST.service.application.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * An implementation of the {@link AuthService} interface, responsible for handling user authentication and authorization.
 * 
 * This service provides methods for authenticating users and verifying user roles.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    /**
     * Authenticates a user with the given username and password, and returns a generated JWT token.
     *
     * @param userName the username to authenticate
     * @param password the password to authenticate
     * @return the generated JWT token
     */
    @Override
    public String authenticate(String userName, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userName, password)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return jwtUtil.generateToken(userDetails);
    }

    /**
     * Retrieves user details based on a given JWT token.
     *
     * @param token the JWT token to identify the user
     * @return the user details associated with the given token
     */
    @Override
    public UserDetails indetify(String token) {
        String userName = jwtUtil.getUserName(token);

        return userDetailsService.loadUserByUsername(userName);
    }

    /**
     * Checks if a user has a specific role.
     *
     * @param userDetails the user details to check
     * @param role        the role to check for
     * @return true if the user has the role, false otherwise
     */
    @Override
    public boolean authorize(UserDetails userDetails, String role) {
        return userDetails.getAuthorities().stream()
            .anyMatch(ga -> ga.getAuthority().equals(role));
    }

    
}
