package com.btg.funds_api.infrastructure.mapper;

import com.btg.funds_api.domain.model.Transaction;
import com.btg.funds_api.infrastructure.config.GlobalMapperConfig;
import com.btg.funds_api.infrastructure.controller.response.TransactionResponse;
import com.btg.funds_api.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface TransactionMapper {
    Transaction toDomain(TransactionEntity entity);
    List<Transaction> toDomainList(List<TransactionEntity> entities);
    TransactionResponse toResponse(Transaction domain);
    List<TransactionResponse> toResponseList(List<Transaction> domains);
    TransactionEntity toEntity(Transaction domain);
}
