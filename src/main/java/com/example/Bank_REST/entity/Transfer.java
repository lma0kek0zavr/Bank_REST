package com.example.Bank_REST.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "from_card_id",
        referencedColumnName = "id"
    )
    private Card fromCard;

    @ManyToOne
    @JoinColumn(
        name = "to_card_id",
        referencedColumnName = "id"
    )
    private Card toCard;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(
        name = "operation_date",
        nullable = false
    )
    private LocalDateTime operationDate;

    @PrePersist
    public void prePersist() {
        this.operationDate = LocalDateTime.now();
    }
}
