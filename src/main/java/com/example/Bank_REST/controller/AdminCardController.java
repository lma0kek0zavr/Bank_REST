package com.example.Bank_REST.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.CardAdminDto;
import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.service.application.AdminCardService;
import com.example.Bank_REST.util.request.CardBlockRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@RequiredArgsConstructor
public class AdminCardController {
    
    private final AdminCardService adminCardService;

    @GetMapping("/{cardId}")
    public ResponseEntity<CardAdminDto> getCard(@PathVariable @NotBlank Long cardId) {
        return ResponseEntity.ok(
            adminCardService.getCard(cardId)
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<CardAdminDto>> getAllCards(
        @RequestParam @Min(0) int page, 
        @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
            adminCardService.getAllCards(page, size)
        );
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CardAdminDto>> getAllCardsByUser(
        @PathVariable @NotBlank Long userId,
        @RequestParam @Min(0) int page, 
        @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
            adminCardService.getAllCardsByUser(userId, page, size)
        );
    }
    
    @PostMapping("/user/{userId}")
    public ResponseEntity<CardDto> createCard(@PathVariable @NotBlank Long userId) {
        return ResponseEntity.ok(
            adminCardService.createCard(userId)
        );
    }

    @PatchMapping("/{cardId}/activate")
    public ResponseEntity<CardDto> activateCard(@PathVariable @NotBlank Long cardId) {
        return ResponseEntity.ok(
            adminCardService.activateCard(cardId)
        );
    }

    @PatchMapping("/block-requests/{requestId}/block")
    public ResponseEntity<CardDto> blockCard(@PathVariable @NotBlank Long requestId) {
        return ResponseEntity.ok(
            adminCardService.blockCard(requestId)
        );
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable @NotBlank Long cardId) {
        adminCardService.deleteCard(cardId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/block-requests/{requestId}")
    public ResponseEntity<CardBlockRequest> getBlockRequest(@PathVariable @NotBlank Long requestId) {
        return ResponseEntity.ok(
            adminCardService.getBlockRequest(requestId)
        );
    }

    @GetMapping("/block-requests")
    public ResponseEntity<List<CardBlockRequest>> getAllBlockRequests() {
        return ResponseEntity.ok(
            adminCardService.getAllBlockRequests()
        );
    }
}
