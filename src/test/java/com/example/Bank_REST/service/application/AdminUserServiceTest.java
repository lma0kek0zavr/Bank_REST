package com.example.Bank_REST.service.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.Bank_REST.dto.UserAdminDto;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.UserMapper;
import com.example.Bank_REST.service.application.impl.AdminUserServiceImpl;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.UserRole;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private User user;
    private UserAdminDto userAdminDto;
    private PageRequest pageRequest;

    @BeforeEach
    void init() {
        user = User.builder()
            .id(1L)
            .userName("testUser")
            .role(UserRole.USER)
            .build();

        userAdminDto = UserAdminDto.builder()
            .id(1L)
            .userName("testUser")
            .role(UserRole.USER)
            .build();

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void getAllUsers_success() {
        when(userDomainService.getAll(pageRequest)).thenReturn(new PageImpl<>(List.of(user), pageRequest, 1L));
        when(userMapper.toAdminDto(user)).thenReturn(userAdminDto);

        Page<UserAdminDto> result = adminUserService.getAllUsers(0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(userAdminDto);
    }

    @Test
    void getUser_success() {
        when(userDomainService.get(1L)).thenReturn(user);
        when(userMapper.toAdminDto(user)).thenReturn(userAdminDto);

        UserAdminDto result = adminUserService.getUser(1L);

        assertThat(result).isEqualTo(userAdminDto);
    }
}
