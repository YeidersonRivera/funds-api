package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.exception.UserException;
import com.btg.funds_api.domain.gateway.TransactionGateway;
import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetTransactionsUseCase {
    private final TransactionGateway transactionGateway;
    private final UserGateway userGateway;

    public List<Transaction> execute(String userId) {

        if (!userGateway.existsById(userId)) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return transactionGateway.getByUserId(userId);
    }
}
