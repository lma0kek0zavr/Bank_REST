package com.example.Bank_REST.service.application.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.dto.CardAdminDto;
import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.mapper.CardMapper;
import com.example.Bank_REST.service.application.AdminCardService;
import com.example.Bank_REST.service.application.CardService;
import com.example.Bank_REST.service.domain.CardDomainService;
import com.example.Bank_REST.util.CardBlockRequestQueue;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.request.CardBlockRequest;

import lombok.RequiredArgsConstructor;

/**
 * An implementation of the {@link AdminCardService} interface, providing administrative functionality for managing cards.
 * 
 * This service offers methods for retrieving, creating, updating, and deleting cards, as well as managing card block requests.
 */
@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {

    private final CardService cardService;

    private final CardDomainService cardDomainService;

    private final CardMapper cardMapper;

    private final CardBlockRequestQueue cbrQueue;

    /**
     * Retrieves a card by its unique identifier.
     *
     * @param cardId the unique identifier of the card
     * @return the card associated with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public CardAdminDto getCard(Long cardId) {
        Card card = cardDomainService.get(cardId);

        return cardMapper.toAdminDto(card);
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
    public Page<CardAdminDto> getAllCards(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Card> cards = cardDomainService.getAll(pageRequest);

        return cards.map(cardMapper::toAdminDto);
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
    public Page<CardAdminDto> getAllCardsByUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Card> cards = cardDomainService.getAllByOwner_Id(userId, pageRequest);

        return cards.map(cardMapper::toAdminDto);
    }

    /**
     * Creates a new card for a user.
     *
     * @param userId the unique identifier of the user
     * @return the newly created card
     */
    @Override
    @Transactional
    public CardDto createCard(Long userId) {
        return cardService.createCard(userId);
    }

    /**
     * Activates a card by its unique identifier.
     *
     * @param cardId the unique identifier of the card
     * @return the activated card
     */
    @Override
    @Transactional
    public CardDto activateCard(Long cardId) {
        return cardService.updateStatus(cardId, CardStatus.ACTIVE);
    }

    /**
     * Blocks a card by its unique request identifier.
     *
     * @param requestId the unique identifier of the block request
     * @return          the blocked card
     */
    @Override
    @Transactional
    public CardDto blockCard(Long requestId) {
        Long cardId = getBlockRequest(requestId).getCardId();
        CardDto card = cardService.updateStatus(cardId, CardStatus.BLOCKED);
        cbrQueue.remove(requestId);

        return card;
    }

    /**
     * Deletes a card by its unique identifier.
     *
     * @param cardId the unique identifier of the card
     */
    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardService.deleteCard(cardId);
    }

    /**
     * Retrieves a card block request by its unique identifier.
     *
     * @param requestId the unique identifier of the block request
     * @return          the card block request associated with the given request ID
     */
    @Override
    public CardBlockRequest getBlockRequest(Long requestId) {
        return cbrQueue.get(requestId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found")
        );
    }

    /**
     * Retrieves a list of all card block requests.
     *
     * @return a list of all card block requests
     */
    @Override
    public List<CardBlockRequest> getAllBlockRequests() {
        return cbrQueue.getAll();
    }
    
}
