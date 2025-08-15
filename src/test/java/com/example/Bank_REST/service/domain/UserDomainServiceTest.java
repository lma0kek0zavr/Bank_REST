package com.example.Bank_REST.service.domain;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.repository.UserRepository;
import com.example.Bank_REST.service.domain.impl.UserDomainServiceImpl;
import com.example.Bank_REST.util.UserRole;

@ExtendWith(MockitoExtension.class)
public class UserDomainServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDomainServiceImpl userDomainService;

    private User testUser;
    private PageRequest pageRequest;

    @BeforeEach
    void init() {
        testUser = User.builder()
            .userName("testUser")
            .role(UserRole.USER)
            .build();

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void getAll_ShouldReturnPageOfUsers() {
        Page<User> usersPage = new PageImpl<>(List.of(testUser), pageRequest, 1);
        when(userRepository.findAll(pageRequest)).thenReturn(usersPage);

        Page<User> result = userDomainService.getAll(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testUser, result.getContent().get(0));
        verify(userRepository).findAll(pageRequest);
    }

    @Test
    void existsByUserName_ShouldReturnTrue_WhenUserExists() {
        when(userRepository.existsByUserName(testUser.getUserName())).thenReturn(true);

        boolean result = userDomainService.existsByUserName(testUser.getUserName());

        assertTrue(result);
        verify(userRepository).existsByUserName(testUser.getUserName());
    }

    @Test
    void existsByUserName_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(userRepository.existsByUserName(testUser.getUserName())).thenReturn(false);

        boolean result = userDomainService.existsByUserName(testUser.getUserName());

        assertFalse(result);
        verify(userRepository).existsByUserName(testUser.getUserName());
    }

    @Test
    void getByUserName_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(testUser));

        User result = userDomainService.getByUserName(testUser.getUserName());

        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository).findByUserName(testUser.getUserName());
    }

    @Test
    void getByUserName_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userDomainService.getByUserName(testUser.getUserName()));
        verify(userRepository).findByUserName(testUser.getUserName());
    }
}
