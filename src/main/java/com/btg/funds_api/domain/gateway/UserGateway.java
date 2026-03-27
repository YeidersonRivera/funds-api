package com.btg.funds_api.domain.gateway;

import com.btg.funds_api.domain.model.User;

public interface UserGateway {
    User create(User user);
    User getById(String id);
    boolean existsById(String Id);
    void update(User user);
    User getByEmail(String email);
}
