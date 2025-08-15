package com.example.Bank_REST.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.CardAdminDto;
import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.service.application.AdminCardService;
import com.example.Bank_REST.util.request.CardBlockRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminCardController {
    
    private final AdminCardService adminCardService;

    @GetMapping("/q=")
    public ResponseEntity<CardAdminDto> getCard(@RequestParam Long cardId) {
        return ResponseEntity.ok(
            adminCardService.getCard(cardId)
        );
    }
    
    @GetMapping("/all/")
    public ResponseEntity<Page<CardAdminDto>> getAllCards(
        @RequestParam int page, 
        @RequestParam int size
    ) {
        return ResponseEntity.ok(
            adminCardService.getAllCards(page, size)
        );
    }
    
    @GetMapping("/all/q=")
    public ResponseEntity<Page<CardAdminDto>> getAllCardsByUser(
        @RequestParam Long userId,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return ResponseEntity.ok(
            adminCardService.getAllCardsByUser(userId, page, size)
        );
    }
    
    @PostMapping("/create/q=")
    public ResponseEntity<CardDto> createCard(@RequestParam Long userId) {
        return ResponseEntity.ok(
            adminCardService.createCard(userId)
        );
    }

    @PatchMapping("/activate/q=")
    public ResponseEntity<CardDto> activateCard(@RequestParam Long cardId) {
        return ResponseEntity.ok(
            adminCardService.activateCard(cardId)
        );
    }

    @PatchMapping("/block/q=")
    public ResponseEntity<CardDto> blockCard(
        @RequestParam Long requestId
    ) {
        return ResponseEntity.ok(
            adminCardService.blockCard(requestId)
        );
    }

    @DeleteMapping("/delete/q=")
    public ResponseEntity<Void> deleteCard(@RequestParam Long cardId) {
        adminCardService.deleteCard(cardId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requests/q=")
    public ResponseEntity<CardBlockRequest> getAllBlockRequests(@RequestParam Long requestId) {
        return ResponseEntity.ok(
            adminCardService.getBlockRequest(requestId)
        );
    }

    @GetMapping("/requests/all/")
    public ResponseEntity<List<CardBlockRequest>> getAllBlockRequests() {
        return ResponseEntity.ok(
            adminCardService.getAllBlockRequests()
        );
    }
}
