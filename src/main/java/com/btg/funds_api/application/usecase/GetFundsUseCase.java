package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.gateway.FundGateway;
import com.btg.funds_api.domain.model.Fund;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetFundsUseCase {

    private final FundGateway fundGateway;

    public List<Fund> execute() {
        return fundGateway.getAll();
    }
}
