package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.exception.TransactionException;
import com.btg.funds_api.domain.gateway.FundGateway;
import com.btg.funds_api.domain.gateway.NotificationGateway;
import com.btg.funds_api.domain.gateway.TransactionGateway;
import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CancelSubscriptionUseCase - Pruebas Unitarias")
class CancelSubscriptionUseCaseTest {
    @Mock
    private TransactionGateway transactionGateway;
    @Mock
    private UserGateway userGateway;
    @Mock
    private FundGateway fundGateway;
    @Mock
    private NotificationGateway notificationGateway;

    @InjectMocks
    private CancelCreateSubscriptionUseCase useCase;

    private User user;
    private Fund fund;
    private Transaction activeTransaction;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-1")
                .name("Yeiderson")
                .email("y@test.com")
                .phone("3001234567")
                .balance(425000.0)
                .notificationPreference(NotificationPreference.SMS)
                .build();

        fund = Fund.builder()
                .id("fund-1")
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .minAmount(75000.0)
                .category("FPV")
                .build();

        activeTransaction = Transaction.builder()
                .id("tx-1")
                .userId("user-1")
                .fundId("fund-1")
                .fundName("FPV_BTG_PACTUAL_RECAUDADORA")
                .amount(75000.0)
                .type(TransactionType.OPEN)
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Cancelación exitosa retorna el saldo al usuario")
    void shouldCancelSuccessfully() {
        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.of(activeTransaction));
        when(transactionGateway.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        useCase.execute("user-1", "fund-1");

        assertThat(user.getBalance()).isEqualTo(500000.0);
        verify(transactionGateway).save(argThat(t ->
                t.getType() == TransactionType.CANCEL &&
                        t.getAmount().equals(75000.0)));
        verify(userGateway).update(user);
        verify(notificationGateway).send(eq(user), eq(fund), anyString());
    }

    @Test
    @DisplayName("Lanza excepción si no hay suscripción activa")
    void shouldThrowWhenNoActiveSubscription() {
        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("user-1", "fund-1"))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining(TransactionException.CANCELLATION_NOT_ALLOWED);

        verify(transactionGateway, never()).save(any());
        verify(userGateway, never()).update(any());
        verify(notificationGateway, never()).send(any(), any(), any());
    }

    @Test
    @DisplayName("Lanza excepción si la última transacción ya es CANCEL")
    void shouldThrowWhenAlreadyCancelled() {
        activeTransaction.setType(TransactionType.CANCEL);

        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.of(activeTransaction));

        assertThatThrownBy(() -> useCase.execute("user-1", "fund-1"))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining(TransactionException.CANCELLATION_NOT_ALLOWED);

        verify(transactionGateway, never()).save(any());
        verify(userGateway, never()).update(any());
    }
}
