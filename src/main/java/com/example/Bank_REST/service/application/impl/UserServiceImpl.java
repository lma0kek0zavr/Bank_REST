package com.example.Bank_REST.service.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.dto.UserDto;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.UserMapper;
import com.example.Bank_REST.service.application.UserService;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.SecurityUtils;
import com.example.Bank_REST.util.UserRole;

import lombok.RequiredArgsConstructor;

/**
 * An implementation of the {@link UserService} interface, responsible for managing user-related operations.
 * 
 * This service provides methods for registering new users, retrieving user information, and performing other user-related tasks.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserDomainService userDomainService;

    private final UserMapper userMapper;

    private final SecurityUtils securityUtils;
    
    /**
     * Registers a new user with the given username and password.
     *
     * @param userName the username to register
     * @param password the password to register
     * @return the registered user's details
     */
    @Override
    @Transactional
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

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return the user with the specified identifier, mapped to a UserDto
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        User user = userDomainService.get(id);

        return userMapper.toDto(user);
    }

    /**
     * Retrieves a page of users based on the provided pagination information.
     *
     * @param page  the page number to retrieve
     * @param size  the number of users per page
     * @return      a page of users
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return userDomainService.getAll(pageRequest)
            .map(userMapper::toDto);
    }
}
