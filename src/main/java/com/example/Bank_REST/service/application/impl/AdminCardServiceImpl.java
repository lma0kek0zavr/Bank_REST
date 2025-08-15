package com.example.Bank_REST.service.application.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {

    private final CardService cardService;

    private final CardDomainService cardDomainService;

    private final CardMapper cardMapper;

    private final CardBlockRequestQueue cbrQueue;

    @Override
    public CardAdminDto getCard(Long cardId) {
        Card card = cardDomainService.get(cardId);

        return cardMapper.toAdminDto(card);
    }

    @Override
    public Page<CardAdminDto> getAllCards(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Card> cards = cardDomainService.getAll(pageRequest);

        return cards.map(cardMapper::toAdminDto);
    }

    @Override
    public Page<CardAdminDto> getAllCardsByUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Card> cards = cardDomainService.getAllByOwner_Id(userId, pageRequest);

        return cards.map(cardMapper::toAdminDto);
    }

    @Override
    public CardDto createCard(Long userId) {
        return cardService.createCard(userId);
    }

    @Override
    public CardDto activateCard(Long cardId) {
        return cardService.updateStatus(cardId, CardStatus.ACTIVE);
    }

    @Override
    public CardDto blockCard(Long requestId) {
        Long cardId = getBlockRequest(requestId).getCardId();
        CardDto card = cardService.updateStatus(cardId, CardStatus.BLOCKED);
        cbrQueue.remove(requestId);

        return card;
    }

    @Override
    public void deleteCard(Long cardId) {
        cardService.deleteCard(cardId);
    }

    @Override
    public CardBlockRequest getBlockRequest(Long requestId) {
        return cbrQueue.get(requestId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found")
        );
    }

    @Override
    public List<CardBlockRequest> getAllBlockRequests() {
        return cbrQueue.getAll();
    }
    
}
