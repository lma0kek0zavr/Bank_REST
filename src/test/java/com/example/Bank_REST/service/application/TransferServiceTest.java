package com.example.Bank_REST.service.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Bank_REST.dto.CardTransferDto;
import com.example.Bank_REST.dto.TransferDto;
import com.example.Bank_REST.entity.Card;
import com.example.Bank_REST.entity.Transfer;
import com.example.Bank_REST.mapper.TransferMapper;
import com.example.Bank_REST.repository.TransferRepository;
import com.example.Bank_REST.service.application.impl.TransferServiceImpl;
import com.example.Bank_REST.service.domain.CardDomainService;
import com.example.Bank_REST.util.CardStatus;
import com.example.Bank_REST.util.request.TransferRequest;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private TransferMapper transferMapper;

    @Mock
    private CardDomainService cardDomainService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void transfer_success() {
        Card fromCard = mock(Card.class);
        Card toCard = mock(Card.class);

        when(cardDomainService.get(1L)).thenReturn(fromCard);
        when(cardDomainService.get(2L)).thenReturn(toCard);

        when(fromCard.getStatus()).thenReturn(CardStatus.ACTIVE);
        when(toCard.getStatus()).thenReturn(CardStatus.ACTIVE);

        when(fromCard.getBalance()).thenReturn(BigDecimal.valueOf(10000.00));
        when(toCard.getBalance()).thenReturn(BigDecimal.valueOf(0.00));

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(1000.00));

        Transfer transferEntity = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(BigDecimal.valueOf(1000.00))
                .build();

        TransferDto transferDto = TransferDto.builder()
                .fromCard(CardTransferDto.builder().id(1L).build())
                .toCard(CardTransferDto.builder().id(2L).build())
                .amount(BigDecimal.valueOf(1000.00))
                .build();

        when(transferRepository.save(any(Transfer.class))).thenReturn(transferEntity);
        when(transferMapper.toDto(transferEntity)).thenReturn(transferDto);

        TransferDto result = transferService.transfer(request);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(1000.00));

        verify(cardService).updateBalance(2L, BigDecimal.valueOf(1000.00));
        verify(cardService).updateBalance(1L, BigDecimal.valueOf(9000.00));

        verify(transferRepository).save(any(Transfer.class));
    }
}
