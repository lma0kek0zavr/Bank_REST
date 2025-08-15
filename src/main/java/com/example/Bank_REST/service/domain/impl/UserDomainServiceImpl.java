package com.example.Bank_REST.service.domain.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.repository.UserRepository;
import com.example.Bank_REST.service.domain.AbstractDomainService;
import com.example.Bank_REST.service.domain.UserDomainService;

@Service
public class UserDomainServiceImpl 
    extends AbstractDomainService<User, Long>
    implements UserDomainService {

    private final UserRepository userRepository;

    public UserDomainServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
    }
    
}
