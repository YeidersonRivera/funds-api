package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.NotificationPreference;
import com.btg.funds_api.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase - Pruebas Unitarias")
class CreateUserUseCaseTest {
    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    @DisplayName("Crea usuario con saldo inicial de 500.000")
    void shouldCreateUserWithInitialBalance() {
        User input = User.builder()
                .name("Yeiderson")
                .email("y@test.com")
                .phone("3001234567")
                .notificationPreference(NotificationPreference.EMAIL)
                .build();

        when(userGateway.create(any(User.class)))
                .thenAnswer(inv -> {
                    User u = inv.getArgument(0);
                    u.setId("user-1");
                    return u;
                });

        User result = useCase.execute(input);

        assertThat(result.getBalance()).isEqualTo(500000.0);
        assertThat(result.getId()).isEqualTo("user-1");
        verify(userGateway).create(argThat(u -> u.getBalance() == 500000.0));
    }
}
