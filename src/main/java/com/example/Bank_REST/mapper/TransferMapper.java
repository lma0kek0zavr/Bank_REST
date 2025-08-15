package com.example.Bank_REST.mapper;

import org.mapstruct.Mapper;

import com.example.Bank_REST.dto.TransferDto;
import com.example.Bank_REST.entity.Transfer;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferDto toDto(Transfer transfer);

    Transfer toEntity(TransferDto transferDto);
}
