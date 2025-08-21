package com.example.Bank_REST.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.UserDto;
import com.example.Bank_REST.service.application.AuthService;
import com.example.Bank_REST.service.application.UserService;
import com.example.Bank_REST.util.request.AuthRequest;
import com.example.Bank_REST.util.response.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/sign-up")
    public UserDto singUp(@RequestBody @Valid AuthRequest req) {
        return userService.register(req.getUserName(), req.getPassword());
    }

    @PostMapping("/sign-in")
    public AuthResponse singIn(@RequestBody @Valid AuthRequest req) {
        String token = authService.authenticate(req.getUserName(), req.getPassword());
        
        return new AuthResponse(token);
    }
}
