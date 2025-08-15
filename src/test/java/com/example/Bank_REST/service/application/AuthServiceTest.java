package com.example.Bank_REST.service.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.Bank_REST.security.JwtUtil;
import com.example.Bank_REST.service.application.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthServiceImpl authService;

    private String userName = "testUser";
    private String password = "testPassword";
    private String token = "testToken";

    @Test
    void authenticate_Success() {
        when(userDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(token);

        String result = authService.authenticate(userName, password);

        assertThat(result).isEqualTo(token);
        verify(userDetailsService).loadUserByUsername(userName);
        verify(jwtUtil).generateToken(userDetails);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }

    @Test
    void indetify_Success() {
        when(jwtUtil.getUserName(token)).thenReturn(userName);
        when(userDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);

        UserDetails result = authService.indetify(token);

        assertThat(result).isEqualTo(userDetails);
        verify(jwtUtil).getUserName(token);
    }
}
