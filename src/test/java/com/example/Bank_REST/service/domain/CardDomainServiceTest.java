package com.example.Bank_REST.service.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
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

import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.entity.User;
import com.example.Bank_REST.repository.CardRepository;
import com.example.Bank_REST.service.domain.impl.CardDomainServiceImpl;
import com.example.Bank_REST.util.UserRole;

@ExtendWith(MockitoExtension.class)
public class CardDomainServiceTest {
    
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardDomainServiceImpl cardDomainService;

    private Card testCard;
    private User testUser;
    private PageRequest pageRequest;

    @BeforeEach
    void init() {
        testUser = User.builder()
            .userName("testUser")
            .role(UserRole.USER)
            .build();

        testCard = Card.builder()
            .number("testCard")
            .owner(testUser)
            .build();

        testUser.setCards(List.of(testCard));

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void existsByNumber_ShouldReturnTrue_WhenCardExists() {
        when(cardRepository.existsByNumber("testCard")).thenReturn(true);

        boolean result = cardDomainService.existsByNumber("testCard");

        assertTrue(result);
        verify(cardRepository).existsByNumber("testCard");
    }

    @Test
    void existsByNumber_ShouldReturnFalse_WhenCardDoesNotExist() {
        when(cardRepository.existsByNumber("testCard")).thenReturn(false);

        boolean result = cardDomainService.existsByNumber("testCard");

        assertFalse(result);
        verify(cardRepository).existsByNumber("testCard");
    }

    @Test
    void getAllByOwner_Id_ShouldReturnPageOfCards() {
        Page<Card> cardsPage = new PageImpl<>(List.of(testCard), pageRequest, 1);
        when(cardRepository.findAllByOwner_Id(testUser.getId(), pageRequest)).thenReturn(cardsPage);

        Page<Card> result = cardDomainService.getAllByOwner_Id(testUser.getId(), pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testCard, result.getContent().get(0));
        verify(cardRepository).findAllByOwner_Id(testUser.getId(), pageRequest);
    }

    @Test
    void getAll_ShouldReturnPageOfCards() {
        Page<Card> cardsPage = new PageImpl<>(List.of(testCard), pageRequest, 1);
        when(cardRepository.findAll(pageRequest)).thenReturn(cardsPage);

        Page<Card> result = cardDomainService.getAll(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testCard, result.getContent().get(0));
        verify(cardRepository).findAll(pageRequest);
    }
}
