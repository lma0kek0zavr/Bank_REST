package com.example.Bank_REST.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank_REST.dto.UserAdminDto;
import com.example.Bank_REST.service.application.AdminUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminUserService adminUserService;

    @GetMapping("/q=")
    public ResponseEntity<UserAdminDto> getUser(@RequestParam Long userId) {
        return ResponseEntity.ok(
            adminUserService.getUser(userId)
        );
    }
    
    @GetMapping("/all")
    public ResponseEntity<Page<UserAdminDto>> getAllUsers(
        @RequestParam int page, 
        @RequestParam int size
    ) {
        return ResponseEntity.ok(
            adminUserService.getAllUsers(page, size)
        );
    }

    @DeleteMapping("/delete/q=")
    public ResponseEntity<Void> deleteUser(@RequestParam Long userId) {
        adminUserService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
