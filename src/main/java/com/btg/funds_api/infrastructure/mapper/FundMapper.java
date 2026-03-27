package com.btg.funds_api.infrastructure.mapper;

import com.btg.funds_api.domain.model.Fund;
import com.btg.funds_api.infrastructure.config.GlobalMapperConfig;
import com.btg.funds_api.infrastructure.controller.response.FundResponse;
import com.btg.funds_api.infrastructure.persistence.entity.FundEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface FundMapper {
    Fund toDomain(FundEntity entity);
    List<Fund> toDomainList(List<FundEntity> entities);
    List<FundResponse> toResponseList(List<Fund> domains);
}
