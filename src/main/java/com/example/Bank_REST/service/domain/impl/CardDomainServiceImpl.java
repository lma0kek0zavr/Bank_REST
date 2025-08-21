package com.example.Bank_REST.service.domain.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.exception.CardNotFoundException;
import com.example.Bank_REST.repository.CardRepository;
import com.example.Bank_REST.service.domain.AbstractDomainService;
import com.example.Bank_REST.service.domain.CardDomainService;

/**
 * An implementation of the {@link CardDomainService} interface, and child class of {@link AbstractDomainService},
 * responsible for managing card-related operations at the domain level.
 * 
 * This service provides methods for creating, updating, deleting, and retrieving Card entities, 
 * and is focused on the business logic and rules related to card management, 
 * rather than the underlying data access or infrastructure.
 */
@Service
public class CardDomainServiceImpl 
    extends AbstractDomainService<Card, Long>
    implements CardDomainService {

    private final CardRepository cardRepository;
    
    public CardDomainServiceImpl(CardRepository cardRepository) {
        super(cardRepository);
        this.cardRepository = cardRepository;
    }

    /**
     * Retrieves a Card entity by its unique identifier.
     *
     * @param id the unique identifier of the Card entity
     * @return the Card entity associated with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public Card get(Long id) {
        return super.repository.findById(id).orElseThrow(
            () -> new CardNotFoundException("Card with such id not found")
        );
    }

    /**
     * Checks if a card with the given number already exists.
     *
     * @param number the card number to check for existence
     * @return true if a card with the given number exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    /**
     * Retrieves a page of Card entities based on the provided page request.
     *
     * @param pageRequest the page request containing pagination information
     * @return a page of Card entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Card> getAll(PageRequest pageRequest) {
        return cardRepository.findAll(pageRequest);
    }

    /**
     * Retrieves a page of Card entities based on the provided owner identifier and page request.
     *
     * @param id            the unique identifier of the User
     * @param pageRequest   the page request containing pagination information
     * @return              a page of Card entities associated with the given owner identifier
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Card> getAllByOwner_Id(Long id, PageRequest pageRequest) {
        return cardRepository.findAllByOwner_Id(id, pageRequest);
    }
    
    /**
     * Deletes a card by their ID.
     *
     * @param id the ID of the user to delete
     * @return nothing, but throws an exception if the card is not found
     */
    @Override
    @Transactional
    public void delete(Long id) {
        super.repository.findById(id).map(
            card -> {
                super.repository.delete(card);
                return card;
            }
        ).orElseThrow(
            () -> new CardNotFoundException("Card with such id not found")
        );
    }
}
