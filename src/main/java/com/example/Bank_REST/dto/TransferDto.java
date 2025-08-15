package com.example.Bank_REST.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferDto {
    private Long id;

    private CardTransferDto fromCard;

    private CardTransferDto toCard;

    private BigDecimal amount;

    private LocalDateTime operationDate;
}
