package com.example.Bank_REST.service.domain.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.repository.CardRepository;
import com.example.Bank_REST.service.domain.AbstractDomainService;
import com.example.Bank_REST.service.domain.CardDomainService;

@Service
public class CardDomainServiceImpl 
    extends AbstractDomainService<Card, Long>
    implements CardDomainService {

    private final CardRepository cardRepository;
    
    public CardDomainServiceImpl(CardRepository cardRepository) {
        super(cardRepository);
        this.cardRepository = cardRepository;
    }

    @Override
    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public Page<Card> getAll(PageRequest pageRequest) {
        return cardRepository.findAll(pageRequest);
    }

    @Override
    public Page<Card> getAllByOwner_Id(Long id, PageRequest pageRequest) {
        return cardRepository.findAllByOwner_Id(id, pageRequest);
    }
    
}
