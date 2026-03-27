package com.btg.funds_api.infrastructure.adapter;

import com.btg.funds_api.domain.exception.FundException;
import com.btg.funds_api.domain.gateway.FundGateway;
import com.btg.funds_api.domain.model.Fund;
import com.btg.funds_api.infrastructure.mapper.FundMapper;
import com.btg.funds_api.infrastructure.persistence.FundRepository;
import com.btg.funds_api.infrastructure.persistence.entity.FundEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FundAdapter implements FundGateway {

    private final FundRepository fundRepository;
    private final FundMapper fundMapper;
    @Override
    public List<Fund> getAll() {
        return fundMapper.toDomainList(fundRepository.findAll());
    }

    @Override
    public Fund getById(String id) {
        return fundMapper.toDomain(
                fundRepository.findById(id)
                        .orElseThrow(() -> new FundException(FundException.NOT_FOUND))
        );
    }
}
