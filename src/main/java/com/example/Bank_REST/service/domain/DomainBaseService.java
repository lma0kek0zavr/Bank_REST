package com.example.Bank_REST.service.domain;

import java.util.List;

public interface DomainBaseService<T, ID> {
    T create(T entity);

    T get(ID id);

    List<T> getAll();

    T update(T entity);

    void delete(ID id);
}
