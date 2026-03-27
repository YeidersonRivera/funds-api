package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserByEmailUseCase {
    private final UserGateway userGateway;
    public User execute(String email) {
        return userGateway.getByEmail(email);
    }
}
