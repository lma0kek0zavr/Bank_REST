package com.example.Bank_REST.service.application;

import org.springframework.data.domain.Page;

import com.example.Bank_REST.dto.UserDto;

public interface UserService {
    UserDto register(String userName, String password);

    UserDto getUser(Long id);

    Page<UserDto> getAll(int page, int size);
}
