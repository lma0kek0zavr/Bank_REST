package com.example.Bank_REST.service.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.example.Bank_REST.dto.CardDto;
import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.mapper.CardMapper;
import com.example.Bank_REST.service.application.impl.CardServiceImpl;
import com.example.Bank_REST.service.domain.CardDomainService;
import com.example.Bank_REST.service.domain.UserDomainService;
import com.example.Bank_REST.util.CardBlockRequestQueue;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.SecurityUtils;
import com.example.Bank_REST.util.request.CardBlockRequest;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardDomainService cardDomainService;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private CardBlockRequestQueue cbrQueue;

    @InjectMocks
    private CardServiceImpl cardService;

    private User user;
    private Card card;
    private CardDto cardDto;
    
    private CardBlockRequest cbr = new CardBlockRequest(
        1L, 
        1L, 
        null, 
        null, 
        null
    );

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("Test User");

        card = Card.builder()
                .id(10L)
                .number("encoded-number")
                .maskedNumber("**** **** **** 1234")
                .expiredAt("08/35")
                .balance(BigDecimal.ZERO)
                .status(CardStatus.ACTIVE)
                .owner(user)
                .build();

        cardDto = CardDto.builder()
            .id(1L)
            .build();
    }

    @Test
    void createCard_success() {
        when(userDomainService.get(1L)).thenReturn(user);

        when(cardDomainService.existsByNumber(anyString())).thenReturn(false);

        when(securityUtils.encodeCardNumber(anyString())).thenReturn("encoded-number");
        when(securityUtils.maskCard(anyString())).thenReturn("**** **** **** 1234");

        when(cardDomainService.create(any(Card.class))).thenReturn(card);
        when(cardMapper.toDto(any(Card.class))).thenReturn(cardDto);

        CardDto result = cardService.createCard(1L);

        assertEquals(result, cardDto);

        verify(userDomainService).get(1L);
        verify(cardDomainService).create(any(Card.class));
    }

    @Test
    void updateStatus_success() {
        when(cardDomainService.get(10L)).thenReturn(card);
        when(cardDomainService.update(card)).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        CardDto result = cardService.updateStatus(10L, CardStatus.BLOCKED);

        assertThat(result).isEqualTo(cardDto);
        assertThat(card.getStatus()).isEqualTo(CardStatus.BLOCKED);

        verify(cardDomainService).update(card);
    }

    @Test
    void deleteCard_success() {
        cardService.deleteCard(10L);

        verify(cardDomainService).delete(10L);
    }

    @Test
    void getCard_success() {
        when(cardDomainService.get(10L)).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        CardDto result = cardService.getCard(10L);

        assertThat(result).isEqualTo(cardDto);
    }

    @Test
    void getAll_success() {
        Page<Card> page = new PageImpl<>(List.of(card));
        when(cardDomainService.getAll(PageRequest.of(0, 5))).thenReturn(page);
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        Page<CardDto> result = cardService.getAll(0, 5);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(cardDto);
    }

    @Test
    void getAllByUser_success() {
        Page<Card> page = new PageImpl<>(List.of(card));
        when(cardDomainService.getAllByOwner_Id(eq(1L), eq(PageRequest.of(0, 5)))).thenReturn(page);
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        Page<CardDto> result = cardService.getAllByUser(1L, 0, 5);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(cardDto);
    }

    @Test
    void updateBalance_success() {
        when(cardDomainService.get(10L)).thenReturn(card);
        when(cardDomainService.update(card)).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        CardDto result = cardService.updateBalance(10L, BigDecimal.valueOf(500));

        assertThat(result).isEqualTo(cardDto);
        assertThat(card.getBalance()).isEqualTo(BigDecimal.valueOf(500));
    }

    @Test
    void requestCardBlock_success() {
        when(cbrQueue.add(10L, 1L)).thenReturn(cbr);

        CardBlockRequest result = cardService.requestCardBlock(10L, 1L);

        assertThat(result).isEqualTo(cbr);
    }
}
