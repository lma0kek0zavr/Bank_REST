package com.example.Bank_REST.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bank_REST.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);

    Page<Card> findAll(Pageable pageable);

    Page<Card> findAllByOwner_Id(Long id, Pageable pageable);
}
