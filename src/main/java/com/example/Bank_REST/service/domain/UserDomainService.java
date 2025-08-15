package com.example.Bank_REST.service.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.Bank_REST.entity.User;

public interface UserDomainService extends DomainBaseService<User, Long> {
    Page<User> getAll(PageRequest pageRequest);

    boolean existsByUserName(String userName);

    User getByUserName(String userName);
}
