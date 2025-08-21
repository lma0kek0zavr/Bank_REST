package com.example.Bank_REST.util.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    
    @NotBlank
    private Long fromCardId;

    @NotBlank
    private Long toCardId;

    @PositiveOrZero
    private BigDecimal amount;
}
