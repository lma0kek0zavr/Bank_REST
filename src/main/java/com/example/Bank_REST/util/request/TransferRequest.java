package com.example.Bank_REST.util.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    
    private Long fromCardId;

    private Long toCardId;

    private BigDecimal amount;
}
