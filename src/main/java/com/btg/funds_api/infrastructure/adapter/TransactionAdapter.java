package com.btg.funds_api.infrastructure.adapter;

import com.btg.funds_api.domain.gateway.TransactionGateway;
import com.btg.funds_api.domain.model.Transaction;
import com.btg.funds_api.domain.model.TransactionType;
import com.btg.funds_api.infrastructure.mapper.TransactionMapper;
import com.btg.funds_api.infrastructure.persistence.TransactionRepository;
import com.btg.funds_api.infrastructure.persistence.entity.TransactionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TransactionAdapter implements TransactionGateway {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = transactionMapper.toEntity(transaction);
        return transactionMapper.toDomain(transactionRepository.save(entity));
    }

    @Override
    public List<Transaction> getByUserId(String userId) {
        return transactionMapper.toDomainList(transactionRepository.findByUserId(userId));
    }

    @Override
    public Optional<Transaction> getByUserIdAndFundId(String userId, String fundId) {
        return transactionRepository.findTopByUserIdAndFundIdOrderByDateDesc(userId, fundId)
                .map(transactionMapper::toDomain);
    }
}
