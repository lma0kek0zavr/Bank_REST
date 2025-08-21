package com.example.Bank_REST.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.UserAdminDto;
import com.example.Bank_REST.service.application.AdminUserService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminUserService adminUserService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserAdminDto> getUser(@PathVariable @NotBlank Long userId) {
        return ResponseEntity.ok(
            adminUserService.getUser(userId)
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<UserAdminDto>> getAllUsers(
        @RequestParam @Min(0) int page, 
        @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
            adminUserService.getAllUsers(page, size)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotBlank Long userId) {
        adminUserService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
