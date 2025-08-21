package com.example.Bank_REST.service.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

/**
 * An abstract class providing a basic implementation of the DomainBaseService interface, 
 * offering common methods for managing entities at the domain level.
 * 
 * This class provides a foundation for domain services, encapsulating generic CRUD operations 
 * and allowing subclasses to focus on entity-specific business logic.
 */
@RequiredArgsConstructor
public abstract class AbstractDomainService<T, ID> implements DomainBaseService<T, ID> {
    
    protected final JpaRepository<T, ID> repository;
    
    /**
     * Creates a new entity in the database.
     *
     * @param entity the entity to be created
     * @return the created entity
     */
    @Override
    @Transactional
    public T create(T entity) {
        return repository.save(entity);
    }

    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to retrieve
     * @return the entity associated with the given id, or throws an exception if not found
     */
    @Override
    @Transactional(readOnly = true)
    public T get(ID id) {
        return repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found")
        );
    }

    /**
     * Retrieves all entities from the database.
     *
     * @return         	a list of all entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return repository.findAll();
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to be updated
     * @return the updated entity
     */
    @Override
    @Transactional
    public T update(T entity) {
        return repository.save(entity);
    }

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to delete
     * @return nothing, but throws an exception if the entity is not found
     */
    @Override
    @Transactional
    public void delete(ID id) {
        repository.deleteById(id);
    }
}
