package com.example.Bank_REST.service.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractDomainService<T, ID> implements DomainBaseService<T, ID> {
    
    protected final JpaRepository<T, ID> repository;
    
    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public T get(ID id) {
        return repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found")
        );
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public T update(T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}
