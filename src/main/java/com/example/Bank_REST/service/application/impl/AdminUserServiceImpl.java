package com.example.Bank_REST.service.application.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Bank_REST.dto.UserAdminDto;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.UserMapper;
import com.example.Bank_REST.service.application.AdminUserService;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.UserRole;

import lombok.RequiredArgsConstructor;

/**
 * An implementation of the {@link AdminUserService} interface, providing administrative functionality for managing users.
 * 
 * This service offers methods for retrieving, deleting, and managing users, with a focus on administrative tasks.
 */
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    
    private final UserDomainService userDomainService;

    private final UserMapper userMapper;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return the user with the specified identifier, mapped to a UserAdminDto
     */
    @Override
    @Transactional(readOnly = true)
    public UserAdminDto getUser(Long id) {
        User user = userDomainService.get(id);

        return userMapper.toAdminDto(user);
    }

    /**
     * Retrieves a page of users with role USER based on the provided pagination information.
     *
     * @param page  the page number to retrieve
     * @param size  the number of users per page
     * @return      a page of users
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAdminDto> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<User> users = userDomainService.getAll(pageRequest);

        List<UserAdminDto> filteredUsers = users.stream()
                .filter(user -> user.getRole().equals(UserRole.USER))
                .map(userMapper::toAdminDto)
                .toList();

        return new PageImpl<>(filteredUsers);
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to delete
     * @return nothing, but throws an exception if the user is not found
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDomainService.delete(id);
    }
}
