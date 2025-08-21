package com.example.Bank_REST.service.application.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.CardMapper;
import com.example.Bank_REST.service.application.CardService;
import com.example.Bank_REST.service.domain.CardDomainService;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.CardBlockRequestQueue;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.SecurityUtils;
import com.example.Bank_REST.util.request.CardBlockRequest;

import lombok.RequiredArgsConstructor;

/**
 * An implementation of the {@link CardService} interface, responsible for managing card-related operations.
 * 
 * This service provides methods for creating, updating, deleting, and retrieving cards, as well as managing card status and balance.
 */
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardDomainService cardDomainService;

    private final CardMapper cardMapper;

    private final UserDomainService userDomainService;

    private final SecurityUtils securityUtils;

    private final CardBlockRequestQueue cbrQueue;

    /**
     * Creates a new card for a user.
     * 
     * Newly created card is always active and has zero balance 
     * 
     * @param userId the unique identifier of the user
     * @return the newly created card
     */
    @Override
    @Transactional
    public CardDto createCard(Long userId) {
        User user = userDomainService.get(userId);

        String number;
        String numberToMask;

        do {
            number = generateCardNumber();
            numberToMask = number;
            number = securityUtils.encodeCardNumber(number);
        } while (cardDomainService.existsByNumber(number));

        String maskedNumber = securityUtils.maskCard(numberToMask);
        String expiredAt = LocalDate.now()
            .plusYears(10L)
            .format(DateTimeFormatter.ofPattern("MM/yy"));
        
        Card card = Card.builder()
            .number(number)
            .maskedNumber(maskedNumber)
            .expiredAt(expiredAt)
            .balance(BigDecimal.ZERO)
            .status(CardStatus.ACTIVE)
            .owner(user)
            .build();

        return cardMapper.toDto(cardDomainService.create(card));
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));

            if ((i + 1) % 4 == 0 && i != 15) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * Updates the status of a card by its unique identifier.
     *
     * @param cardId the unique identifier of the card
     * @param newStatus the new status of the card
     * @return the updated card
     */
    @Override
    @Transactional
    public CardDto updateStatus(Long cardId, CardStatus newStatus) {
        Card cardToUpdate = cardDomainService.get(cardId);
        
        cardToUpdate.setStatus(newStatus);
        
        return cardMapper.toDto(cardDomainService.update(cardToUpdate));
    }

    /**
     * Deletes a card by its unique identifier.
     *
     * @param cardId the unique identifier of the card
     * @return      nothing, but throws an exception if the card is not found
     */
    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardDomainService.delete(cardId);
    }

    /**
     * Retrieves a card by its unique identifier.
     *
     * @param cardId the unique identifier of the card
     * @return the card associated with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public CardDto getCard(Long cardId) {
        return cardMapper.toDto(cardDomainService.get(cardId));
    }

    /**
     * Retrieves a page of cards based on the provided pagination information.
     *
     * @param page  the page number to retrieve
     * @param size  the number of cards per page
     * @return      a page of cards
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return cardDomainService.getAll(pageRequest)
            .map(cardMapper::toDto);
    }

    /**
     * Retrieves a page of cards associated with a specific user based on the provided pagination information.
     *
     * @param userId the unique identifier of the user
     * @param page   the page number to retrieve
     * @param size   the number of cards per page
     * @return       a page of cards associated with the given user
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getAllByUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return cardDomainService.getAllByOwner_Id(userId, pageRequest)
            .map(cardMapper::toDto);
    }

    /**
     * Updates the balance of a card by its unique identifier.
     *
     * @param cardId     the unique identifier of the card
     * @param newBalance the new balance of the card
     * @return          the updated card
     */
    @Override
    @Transactional
    public CardDto updateBalance(Long cardId, BigDecimal newBalance) {
        Card cardToUpdate = cardDomainService.get(cardId);
        
        cardToUpdate.setBalance(newBalance);
        
        return cardMapper.toDto(cardDomainService.update(cardToUpdate));
    }

    /**
     * Requests a card block by adding it to the card block request queue.
     *
     * @param cardId    the unique identifier of the card to be blocked
     * @param userId    the unique identifier of the user requesting the block
     * @return          the card block request
     */
    @Override
    public CardBlockRequest requestCardBlock(Long cardId, Long userId) {
        return cbrQueue.add(cardId, userId);
    }
    
}
