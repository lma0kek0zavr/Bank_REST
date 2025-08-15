package com.example.Bank_REST.service.application.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Bank_REST.dto.UserAdminDto;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.UserMapper;
import com.example.Bank_REST.service.application.AdminUserService;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    
    private final UserDomainService userDomainService;

    private final UserMapper userMapper;

    @Override
    public UserAdminDto getUser(Long id) {
        User user = userDomainService.get(id);

        return userMapper.toAdminDto(user);
    }

    @Override
    public Page<UserAdminDto> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<User> users = userDomainService.getAll(pageRequest);

        List<UserAdminDto> filteredUsers = users.stream()
                .filter(user -> user.getRole().equals(UserRole.USER))
                .map(userMapper::toAdminDto)
                .toList();

        return new PageImpl<>(filteredUsers);
    }

    @Override
    public void deleteUser(Long id) {
        userDomainService.delete(id);
    }
}
