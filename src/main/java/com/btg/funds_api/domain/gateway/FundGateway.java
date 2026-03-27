package com.btg.funds_api.domain.gateway;

import com.btg.funds_api.domain.model.Fund;

import java.util.List;

public interface FundGateway {
    List<Fund> getAll();
    Fund getById(String id);
}
