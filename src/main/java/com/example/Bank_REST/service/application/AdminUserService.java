package com.example.Bank_REST.service.application;

import org.springframework.data.domain.Page;

import com.example.Bank_REST.dto.UserAdminDto;

public interface AdminUserService {
    UserAdminDto getUser(Long id);

    Page<UserAdminDto> getAllUsers(int page, int size);

    void deleteUser(Long id);
}
