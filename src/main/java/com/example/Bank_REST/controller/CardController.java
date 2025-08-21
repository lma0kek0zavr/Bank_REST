package com.example.Bank_REST.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.service.application.CardService;
import com.example.Bank_REST.util.SecurityUtils;
import com.example.Bank_REST.util.request.CardBlockRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    
    private final CardService cardService;

    private final SecurityUtils securityUtils;

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDto> viewUserCard(@PathVariable @NotBlank Long cardId) {
        return ResponseEntity.ok(
            cardService.getCard(cardId)
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<CardDto>> viewAllUserCards(
        @RequestParam @Min(0) int page, 
        @RequestParam @Positive int size
    ) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            cardService.getAllByUser(userId, page, size)
        );
    }
    
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<CardBlockRequest> requestCardBlock(@PathVariable @NotBlank Long cardId) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            cardService.requestCardBlock(cardId, userId)
        );
    }
    
}
