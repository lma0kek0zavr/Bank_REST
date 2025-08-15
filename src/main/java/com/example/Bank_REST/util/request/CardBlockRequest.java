package com.example.Bank_REST.util.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardBlockRequest {
    
    private Long id;

    private Long cardId;

    private Long userId;

    private LocalDateTime createdAt;

    private String status;
}
