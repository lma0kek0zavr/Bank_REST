package com.example.Bank_REST.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.TransferDto;
import com.example.Bank_REST.service.application.TransferService;
import com.example.Bank_REST.util.request.TransferRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    
    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferDto> processTransfer(@RequestBody TransferRequest transferRequest) {
        return ResponseEntity.ok(
            transferService.transfer(transferRequest)
        );
    }
    
}
