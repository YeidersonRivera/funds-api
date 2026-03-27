package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.exception.TransactionException;
import com.btg.funds_api.domain.exception.UserException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateSubscriptionUseCase - Pruebas Unitarias")
class CreateSubscriptionUseCaseTest {
    @Mock
    private FundGateway fundGateway;
    @Mock
    private UserGateway userGateway;
    @Mock
    private TransactionGateway transactionGateway;
    @Mock
    private NotificationGateway notificationGateway;

    @InjectMocks
    private CreateSubscriptionUseCase useCase;

    private User user;
    private Fund fund;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-1")
                .name("Yeiderson")
                .email("y@test.com")
                .phone("3001234567")
                .balance(500000.0)
                .notificationPreference(NotificationPreference.EMAIL)
                .build();

        fund = Fund.builder()
                .id("fund-1")
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .minAmount(75000.0)
                .category("FPV")
                .build();
    }

    @Test
    @DisplayName("Suscripción exitosa descuenta el saldo del usuario")
    void shouldSubscribeSuccessfully() {
        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.empty());
        when(transactionGateway.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        useCase.execute("user-1", "fund-1");

        assertThat(user.getBalance()).isEqualTo(425000.0);
        verify(transactionGateway).save(argThat(t ->
                t.getType() == TransactionType.OPEN &&
                        t.getAmount().equals(75000.0)));
        verify(userGateway).update(user);
        verify(notificationGateway).send(eq(user), eq(fund), anyString());
    }

    @Test
    @DisplayName("Lanza excepción si el usuario ya tiene suscripción activa")
    void shouldThrowWhenAlreadySubscribed() {
        Transaction activeTransaction = Transaction.builder()
                .id("tx-1")
                .userId("user-1")
                .fundId("fund-1")
                .type(TransactionType.OPEN)
                .date(LocalDateTime.now())
                .build();

        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.of(activeTransaction));

        assertThatThrownBy(() -> useCase.execute("user-1", "fund-1"))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining(TransactionException.ALREADY_SUBSCRIBED);

        verify(transactionGateway, never()).save(any());
        verify(userGateway, never()).update(any());
        verify(notificationGateway, never()).send(any(), any(), any());
    }

    @Test
    @DisplayName("Lanza excepción si el saldo es insuficiente")
    void shouldThrowWhenInsufficientBalance() {
        user.setBalance(10000.0);

        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("user-1", "fund-1"))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserException.INSUFFICIENT_BALANCE);

        verify(transactionGateway, never()).save(any());
        verify(userGateway, never()).update(any());
    }

    @Test
    @DisplayName("Permite reactivar suscripción si la última fue CANCEL")
    void shouldAllowResubscribeAfterCancel() {
        Transaction cancelledTransaction = Transaction.builder()
                .id("tx-1")
                .userId("user-1")
                .fundId("fund-1")
                .type(TransactionType.CANCEL)
                .date(LocalDateTime.now())
                .build();

        when(userGateway.getById("user-1")).thenReturn(user);
        when(fundGateway.getById("fund-1")).thenReturn(fund);
        when(transactionGateway.getByUserIdAndFundId("user-1", "fund-1"))
                .thenReturn(Optional.of(cancelledTransaction));
        when(transactionGateway.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        useCase.execute("user-1", "fund-1");

        verify(transactionGateway).save(any(Transaction.class));
        verify(userGateway).update(user);
    }
}
