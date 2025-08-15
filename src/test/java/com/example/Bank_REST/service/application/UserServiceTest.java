package com.example.Bank_REST.service.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.example.Bank_REST.dto.UserDto;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.UserMapper;
import com.example.Bank_REST.service.application.impl.UserServiceImpl;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.SecurityUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserDomainService userDomainService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserServiceImpl userService;

    private PageRequest pageRequest = PageRequest.of(0, 10);

    private String userName = "testUser";
    private String password = "testPassword";
    private UserDto userDto;

    @BeforeEach
    void init() {
        userDto = UserDto.builder()
            .userName(userName)
            .build();
    }

    @Test
    void register_Success() {
        when(userDomainService.existsByUserName(userName)).thenReturn(false);
        when(securityUtils.encodePassword(password)).thenReturn("encodedPassword");
        when(userDomainService.create(any(User.class))).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.register(userName, password);

        assertThat(result).isEqualTo(userDto);
        assertNotNull(result);
        verify(userDomainService).create(any(User.class));
    }

    @Test
    void throwException_WhenUserAlreadyExists() {
        when(userDomainService.existsByUserName(userName)).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> userService.register(userName, password));
    }

    @Test
    void getUser_Success() {
        when(userDomainService.get(1L)).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.getUser(1L);

        assertThat(result).isEqualTo(userDto);
        assertNotNull(result);
        verify(userDomainService).get(1L);
    }

    @Test
    void getAll_success() {
        when(userDomainService.getAll(pageRequest)).thenReturn(new PageImpl<>(List.of(new User()), pageRequest, 1L));
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        Page<UserDto> result = userService.getAll(0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(userDto);
        verify(userDomainService).getAll(pageRequest);
    }
}
