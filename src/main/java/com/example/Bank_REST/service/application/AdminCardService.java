package com.example.Bank_REST.service.application;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.Bank_REST.dto.CardAdminDto;
import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.util.request.CardBlockRequest;

public interface AdminCardService {
    CardAdminDto getCard(Long cardId);

    Page<CardAdminDto> getAllCards(int page, int size);

    Page<CardAdminDto> getAllCardsByUser(Long userId, int page, int size);

    CardDto createCard(Long userId);

    CardDto activateCard(Long cardId);

    CardDto blockCard(Long requestId);

    void deleteCard(Long cardId);

    CardBlockRequest getBlockRequest(Long requestId);

    List<CardBlockRequest> getAllBlockRequests();
}