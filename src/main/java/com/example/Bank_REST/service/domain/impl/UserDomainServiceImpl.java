package com.example.Bank_REST.service.domain.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.exception.UserNotFoundException;
import com.example.Bank_REST.repository.UserRepository;
import com.example.Bank_REST.service.domain.AbstractDomainService;
import com.example.Bank_REST.service.domain.UserDomainService;

/**
 * An implementation of the {@link UserDomainService} interface and child class of {@link AbstractDomainService},
 * responsible for managing user-related operations at the domain level.
 * 
 * This service provides methods for creating, updating, deleting, and retrieving User entities, 
 * and is focused on the business logic and rules related to user management, 
 * rather than the underlying data access or infrastructure.
 */
@Service
public class UserDomainServiceImpl 
    extends AbstractDomainService<User, Long>
    implements UserDomainService {

    private final UserRepository userRepository;

    public UserDomainServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID, or throws an exception if not found
     */
    @Override
    @Transactional(readOnly = true)
    public User get(Long id) {
        return super.repository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User with such id not found")
        );
    }

    /**
     * Retrieves a page of users based on the provided page request.
     *
     * @param pageRequest the page request containing pagination details
     * @return a page of users
     */
    @Override
    @Transactional(readOnly = true)
    public Page<User> getAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    /**
     * Checks if a user exists by their username.
     *
     * @param userName the username to check for existence
     * @return true if a user with the given username exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param  userName	the username to retrieve the user for
     * @return         	the user with the specified username, or throws an exception if not found
     */
    @Override
    @Transactional(readOnly = true)
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
    }
    
    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return nothing, but throws an exception if the user is not found
     */
    @Override
    @Transactional
    public void delete(Long id) {
        super.repository.findById(id).map(
            user -> {
                super.repository.delete(user);
                return user;
            }
        ).orElseThrow(
            () -> new UserNotFoundException("User with such id not found")
        );
    }
}
