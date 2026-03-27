package com.btg.funds_api.infrastructure.adapter;

import com.btg.funds_api.domain.exception.UserException;
import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.User;
import com.btg.funds_api.infrastructure.mapper.UserMapper;
import com.btg.funds_api.infrastructure.persistence.UserRepository;
import com.btg.funds_api.infrastructure.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserAdapter implements UserGateway {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public User create(User user) {
        return userMapper.toDomain(userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public User getById(String id) {
        return userMapper.toDomain(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserException(UserException.NOT_FOUND)));
    }

    @Override
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public void update(User user) {
        userRepository.save(userMapper.toEntity(user));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain)
                .orElseThrow(() -> new UserException(UserException.NOT_FOUND));
    }
}
