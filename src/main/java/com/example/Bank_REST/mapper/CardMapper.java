package com.example.Bank_REST.mapper;

import org.mapstruct.Mapper;

import com.example.Bank_REST.dto.CardAdminDto;
import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.dto.CardTransferDto;
import com.example.Bank_REST.entity.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDto toDto(Card card);

    Card toEntity(CardDto cardDto);

    CardAdminDto toAdminDto(Card card);

    CardTransferDto toTransferDto(Card card);
}
