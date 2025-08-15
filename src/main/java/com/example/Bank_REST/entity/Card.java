package com.example.Bank_REST.entity;

import java.math.BigDecimal;

import com.example.Bank_REST.util.CardStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "number",
        nullable = false,
        unique = true
    )
    private String number;

    @Column(
        name = "masked_number",
        nullable = false
    )
    private String maskedNumber;

    @Column(
        nullable = false
    )
    private String expiredAt;

    @Column(
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(
        nullable = false
    )
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
