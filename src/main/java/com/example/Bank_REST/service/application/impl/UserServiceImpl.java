package com.example.Bank_REST.service.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.dto.UserDto;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.UserMapper;
import com.example.Bank_REST.service.application.UserService;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.SecurityUtils;
import com.example.Bank_REST.util.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserDomainService userDomainService;

    private final UserMapper userMapper;

    private final SecurityUtils securityUtils;
    
    @Override
    public UserDto register(String userName, String password) {
        if (userDomainService.existsByUserName(userName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        String encodedPassword = securityUtils.encodePassword(password);

        User user = User.builder()
            .userName(userName)
            .password(encodedPassword)
            .role(UserRole.USER)
            .build();

        return userMapper.toDto(userDomainService.create(user));
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userDomainService.get(id);

        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return userDomainService.getAll(pageRequest)
            .map(userMapper::toDto);
    }
}
