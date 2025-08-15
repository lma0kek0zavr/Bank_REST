package com.example.Bank_REST.service.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.Bank_REST.entity.Card;

public interface CardDomainService extends DomainBaseService<Card, Long> {
    boolean existsByNumber(String number);

    Page<Card> getAll(PageRequest pageRequest);

    Page<Card> getAllByOwner_Id(Long id, PageRequest pageRequest);
}
