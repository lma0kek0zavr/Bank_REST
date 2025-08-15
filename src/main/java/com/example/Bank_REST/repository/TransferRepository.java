package com.example.Bank_REST.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bank_REST.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    
}
