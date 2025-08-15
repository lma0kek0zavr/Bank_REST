package com.example.Bank_REST.mapper;

import org.mapstruct.Mapper;

import com.example.Bank_REST.dto.UserAdminDto;
import com.example.Bank_REST.dto.UserDto;
import com.example.Bank_REST.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    UserAdminDto toAdminDto(User user);
}
