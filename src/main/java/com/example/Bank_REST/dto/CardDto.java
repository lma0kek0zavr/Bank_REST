package com.example.Bank_REST.dto;

import java.math.BigDecimal;

import com.example.Bank_REST.util.CardStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardDto {
    private Long id;

    private String maskedNumber;

    private CardStatus status;

    private BigDecimal balance;
}
