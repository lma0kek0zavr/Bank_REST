package com.example.Bank_REST.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.service.application.CardService;
import com.example.Bank_REST.util.SecurityUtils;
import com.example.Bank_REST.util.request.CardBlockRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class UserController {
    
    private final CardService cardService;

    private final SecurityUtils securityUtils;

    @GetMapping("/view/q=")
    public ResponseEntity<CardDto> viewUserCard(@RequestParam Long cardId) {
        return ResponseEntity.ok(
            cardService.getCard(cardId)
        );
    }
    
    @GetMapping("/view/all/")
    public ResponseEntity<Page<CardDto>> viewAllUserCards(
        @RequestParam int page, 
        @RequestParam int size
    ) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            cardService.getAllByUser(userId, page, size)
        );
    }
    
    @PostMapping("/block/q=")
    public ResponseEntity<CardBlockRequest> requestCardBlock(@RequestParam Long cardId) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            cardService.requestCardBlock(cardId, userId)
        );
    }
    
}
