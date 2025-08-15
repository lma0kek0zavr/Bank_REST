package com.example.Bank_REST.service.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.example.Bank_REST.dto.CardAdminDto;
import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.mapper.CardMapper;
import com.example.Bank_REST.service.application.impl.AdminCardServiceImpl;
import com.example.Bank_REST.service.domain.CardDomainService;
import com.example.Bank_REST.util.CardBlockRequestQueue;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.request.CardBlockRequest;

@ExtendWith(MockitoExtension.class)
public class AdminCardServiceTest {

    @Mock
    private CardService cardService;

    @Mock
    private CardDomainService cardDomainService;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private CardBlockRequestQueue cbrQueue;

    @InjectMocks
    private AdminCardServiceImpl adminCardService;

    private Card testCard;
    private CardAdminDto adminCardDto;
    private CardDto cardDto;
    private PageRequest pageRequest;
    private CardBlockRequest cbr = new CardBlockRequest(
        1L, 
        1L, 
        null, 
        null, 
        null
    );

    @BeforeEach
    void init() {
        testCard = Card.builder()
            .id(1L)
            .maskedNumber("**** **** **** 1234")
            .status(CardStatus.ACTIVE)
            .build();

        adminCardDto = CardAdminDto.builder()
            .id(1L)
            .maskedNumber("**** **** **** 1234")
            .status(CardStatus.ACTIVE)
            .build();

        cardDto = CardDto.builder()
            .id(1L)
            .maskedNumber("**** **** **** 1234")
            .status(CardStatus.ACTIVE)
            .build();

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void get_ShouldReturnCardDto() {
        when(cardDomainService.get(1L)).thenReturn(testCard);
        when(cardMapper.toAdminDto(testCard)).thenReturn(adminCardDto);

        CardAdminDto result = adminCardService.getCard(1L);

        assertNotNull(result);
        assertEquals(testCard.getMaskedNumber(), result.getMaskedNumber());
        verify(cardDomainService).get(1L);
    }

    @Test
    void getAllCards_ShouldReturnCardsPage() {
        Page<Card> testPage = new PageImpl<>(List.of(testCard));
        
        when(cardDomainService.getAll(pageRequest)).thenReturn(testPage);
        when(cardMapper.toAdminDto(testCard)).thenReturn(adminCardDto);

        Page<CardAdminDto> result = adminCardService.getAllCards(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(cardDomainService).getAll(pageRequest);
    }

    @Test
    void getAllByUser_ShouldReturnCardsPage() {
        Page<Card> testPage = new PageImpl<>(List.of(testCard));
        
        when(cardDomainService.getAllByOwner_Id(1L, pageRequest)).thenReturn(testPage);
        when(cardMapper.toAdminDto(testCard)).thenReturn(adminCardDto);

        Page<CardAdminDto> result = adminCardService.getAllCardsByUser(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(cardDomainService).getAllByOwner_Id(1L, pageRequest);
    }

    @Test
    void createCard_ShouldReturnCardDto() {
        when(cardService.createCard(1L)).thenReturn(cardDto);

        CardDto result = adminCardService.createCard(1L);

        assertNotNull(result);
        verify(cardService).createCard(1L);
    }

    @Test
    void blockCard_ShouldReturnCardDtoAndUpdateStatus() {
        when(cbrQueue.get(1L)).thenReturn(Optional.of(cbr));
        when(cardService.updateStatus(1L, CardStatus.BLOCKED)).thenReturn(cardDto);

        CardDto result = adminCardService.blockCard(1L);

        assertEquals(result, cardDto);
        verify(cbrQueue).get(1L);
        verify(cardService).updateStatus(1L, CardStatus.BLOCKED);
        verify(cbrQueue).remove(1L);
    }

    @Test
    void getBlockRequest_shouldThrow_whenNotFound() {
        when(cbrQueue.get(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> adminCardService.getBlockRequest(1L));
    }

    @Test
    void activateCard_shouldReturnCardDtoAndUpdateStatus() {
        when(cardService.updateStatus(1L, CardStatus.ACTIVE)).thenReturn(cardDto);

        CardDto result = adminCardService.activateCard(1L);

        assertEquals(result, cardDto);
        verify(cardService).updateStatus(1L, CardStatus.ACTIVE);
    }
}
