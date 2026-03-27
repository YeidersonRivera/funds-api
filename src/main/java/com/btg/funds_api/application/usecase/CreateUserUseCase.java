package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserUseCase {
    private final UserGateway userGateway;
    public User execute(User user) {
        user.setBalance(500000.0);
        return userGateway.create(user);
    }
}
