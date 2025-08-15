package com.example.Bank_REST.service.application.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardDomainService cardDomainService;

    private final CardMapper cardMapper;

    private final UserDomainService userDomainService;

    private final SecurityUtils securityUtils;

    private final CardBlockRequestQueue cbrQueue;

    @Override
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

    @Override
    public CardDto updateStatus(Long cardId, CardStatus newStatus) {
        Card cardToUpdate = cardDomainService.get(cardId);
        
        cardToUpdate.setStatus(newStatus);
        
        return cardMapper.toDto(cardDomainService.update(cardToUpdate));
    }

    @Override
    public void deleteCard(Long cardId) {
        cardDomainService.delete(cardId);
    }

    @Override
    public CardDto getCard(Long cardId) {
        return cardMapper.toDto(cardDomainService.get(cardId));
    }

    @Override
    public Page<CardDto> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return cardDomainService.getAll(pageRequest)
            .map(cardMapper::toDto);
    }

    @Override
    public Page<CardDto> getAllByUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return cardDomainService.getAllByOwner_Id(userId, pageRequest)
            .map(cardMapper::toDto);
    }

    @Override
    public CardDto updateBalance(Long cardId, BigDecimal newBalance) {
        Card cardToUpdate = cardDomainService.get(cardId);
        
        cardToUpdate.setBalance(newBalance);
        
        return cardMapper.toDto(cardDomainService.update(cardToUpdate));
    }

    @Override
    public CardBlockRequest requestCardBlock(Long cardId, Long userId) {
        return cbrQueue.add(cardId, userId);
    }
    
}
