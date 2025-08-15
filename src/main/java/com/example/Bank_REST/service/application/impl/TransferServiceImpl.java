package com.example.Bank_REST.service.application.impl;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Bank_REST.dto.TransferDto;
import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.entity.Transfer;
import com.example.Bank_REST.mapper.TransferMapper;
import com.example.Bank_REST.repository.TransferRepository;
import com.example.Bank_REST.service.application.CardService;
import com.example.Bank_REST.service.application.TransferService;
import com.example.Bank_REST.service.domain.CardDomainService;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.request.TransferRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    
    private final TransferRepository transferRepository;

    private final TransferMapper transferMapper;

    private final CardDomainService cardDomainService;

    private final CardService cardService;
    
    @Override
    public TransferDto transfer(TransferRequest transferRequest) {
        Long fromCardId = transferRequest.getFromCardId();
        Long toCardId = transferRequest.getToCardId();
        BigDecimal amount = transferRequest.getAmount();
        
        Card fromCard = cardDomainService.get(fromCardId);
        Card toCard = cardDomainService.get(toCardId);
        
        if (fromCard.getStatus() == CardStatus.BLOCKED 
            || toCard.getStatus() == CardStatus.BLOCKED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card is blocked");
        }
        
        if (fromCard.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not enough money");
        }
        
        BigDecimal fromCardBalance = fromCard.getBalance();
        BigDecimal toCardBalance = toCard.getBalance();
        
        fromCardBalance = fromCardBalance.subtract(amount);
        toCardBalance = toCardBalance.add(amount);
        
        fromCard.setBalance(fromCardBalance);
        toCard.setBalance(toCardBalance);
        
        cardService.updateBalance(toCardId, toCardBalance);
        cardService.updateBalance(fromCardId, fromCardBalance);
        
        Transfer transfer = Transfer.builder()
            .fromCard(fromCard)
            .toCard(toCard)
            .amount(amount)
            .build();

        return transferMapper.toDto(transferRepository.save(transfer));
    }
    
}
