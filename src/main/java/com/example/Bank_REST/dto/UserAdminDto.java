package com.example.Bank_REST.dto;

import java.util.List;

import com.example.Bank_REST.util.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAdminDto {
    private Long id;

    private String userName;

    private UserRole role;

    private List<CardDto> cards;
}
