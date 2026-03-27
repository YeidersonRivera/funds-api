package com.btg.funds_api.infrastructure.mapper;

import com.btg.funds_api.domain.model.User;
import com.btg.funds_api.infrastructure.config.GlobalMapperConfig;
import com.btg.funds_api.infrastructure.controller.request.UserRequest;
import com.btg.funds_api.infrastructure.controller.response.UserResponse;
import com.btg.funds_api.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper {
    User toDomain(UserEntity entity);
    UserResponse toResponse(User domain);
    User requestToDomain(UserRequest request);
    UserEntity toEntity(User domain);
}
