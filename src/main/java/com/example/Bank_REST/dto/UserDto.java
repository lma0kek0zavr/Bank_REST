package com.example.Bank_REST.dto;

import com.example.Bank_REST.util.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;

    private String userName;

    private UserRole role;
}
