package com.btg.funds_api.domain.gateway;

import com.btg.funds_api.domain.model.Transaction;
import com.btg.funds_api.domain.model.TransactionType;

import java.util.List;
import java.util.Optional;

public interface TransactionGateway {
    Transaction save(Transaction transaction);
    List<Transaction> getByUserId(String userId);
    Optional<Transaction> getByUserIdAndFundId(String userId, String fundId);
}
