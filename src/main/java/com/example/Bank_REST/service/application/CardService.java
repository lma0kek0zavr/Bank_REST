package com.example.Bank_REST.service.application;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.request.CardBlockRequest;

public interface CardService {
    CardDto createCard(Long userId);

    CardDto updateStatus(Long cardId, CardStatus newStatus);

    CardDto updateBalance(Long cardId, BigDecimal newBalance);

    void deleteCard(Long cardId);

    CardDto getCard(Long cardId);

    Page<CardDto> getAll(int page, int size);

    Page<CardDto> getAllByUser(Long userId, int page, int size);

    CardBlockRequest requestCardBlock(Long cardId, Long userId);
}
