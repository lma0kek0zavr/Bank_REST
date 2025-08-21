package com.example.Bank_REST.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.service.domain.UserDomainService;

import lombok.RequiredArgsConstructor;

/**
 * A custom implementation of the {@link UserDetailsService} interface, responsible for loading user details by username.
 * 
 * This service is used by the Spring Security framework to authenticate users and load their corresponding user details.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserDomainService userDomainService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDomainService.getByUserName(username);

        org.springframework.security.core.userdetails.User userPrincipals = 
            new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );

        return userPrincipals;
    }
    
}
