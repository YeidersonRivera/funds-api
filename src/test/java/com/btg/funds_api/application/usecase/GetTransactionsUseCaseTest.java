package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.exception.UserException;
import com.btg.funds_api.domain.gateway.TransactionGateway;
import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.Transaction;
import com.btg.funds_api.domain.model.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetTransactionsUseCase - Pruebas Unitarias")
class GetTransactionsUseCaseTest {
    @Mock
    private TransactionGateway transactionGateway;
    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private GetTransactionsUseCase useCase;

    @Test
    @DisplayName("Retorna historial de transacciones del usuario")
    void shouldReturnTransactions() {
        List<Transaction> transactions = List.of(
                Transaction.builder().id("tx-1").userId("user-1")
                        .type(TransactionType.OPEN)
                        .amount(75000.0)
                        .date(LocalDateTime.now()).build(),
                Transaction.builder().id("tx-2").userId("user-1")
                        .type(TransactionType.CANCEL)
                        .amount(75000.0)
                        .date(LocalDateTime.now()).build()
        );

        when(userGateway.existsById("user-1")).thenReturn(true);
        when(transactionGateway.getByUserId("user-1")).thenReturn(transactions);

        List<Transaction> result = useCase.execute("user-1");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getType()).isEqualTo(TransactionType.OPEN);
        assertThat(result.get(1).getType()).isEqualTo(TransactionType.CANCEL);
        verify(transactionGateway).getByUserId("user-1");
    }

    @Test
    @DisplayName("Lanza excepción si el usuario no existe")
    void shouldThrowWhenUserNotFound() {
        when(userGateway.existsById("user-99")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("user-99"))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserException.NOT_FOUND);

        verify(transactionGateway, never()).getByUserId(any());
    }
}
