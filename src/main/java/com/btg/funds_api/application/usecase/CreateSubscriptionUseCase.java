package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.exception.TransactionException;
import com.btg.funds_api.domain.exception.UserException;
import com.btg.funds_api.domain.gateway.FundGateway;
import com.btg.funds_api.domain.gateway.NotificationGateway;
import com.btg.funds_api.domain.gateway.TransactionGateway;
import com.btg.funds_api.domain.gateway.UserGateway;
import com.btg.funds_api.domain.model.Fund;
import com.btg.funds_api.domain.model.Transaction;
import com.btg.funds_api.domain.model.TransactionType;
import com.btg.funds_api.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CreateSubscriptionUseCase {
    private final FundGateway fundGateway;
    private final UserGateway userGateway;
    private final TransactionGateway transactionGateway;
    private final NotificationGateway notificationGateway;

    public void execute(String userId, String fundId) {
        User user = userGateway.getById(userId);

        Fund fund = fundGateway.getById(fundId);

        Optional<Transaction> lastTransaction = transactionGateway.getByUserIdAndFundId(
                user.getId(), fund.getId()
        );

        if (lastTransaction.isPresent() && lastTransaction.get().getType() == TransactionType.OPEN) {
            throw new TransactionException(TransactionException.ALREADY_SUBSCRIBED);
        }

        if (user.getBalance() < fund.getMinAmount()) {
            throw new UserException(UserException.INSUFFICIENT_BALANCE);
        }

        Transaction transaction = Transaction.builder()
                .userId(user.getId())
                .fundId(fund.getId())
                .fundName(fund.getName())
                .amount(fund.getMinAmount())
                .type(TransactionType.OPEN)
                .date(LocalDateTime.now())
                .build();

        Transaction createdTransaction = transactionGateway.save(transaction);

        user.setBalance(user.getBalance() - createdTransaction.getAmount());

        userGateway.update(user);

        try {
            String message = String.format(
                    NotificationGateway.SUBSCRIPTION_MESSAGE,
                    fund.getName(), fund.getMinAmount());
            notificationGateway.send(user, fund, message);
        } catch (Exception e) {
            log.warn("No se pudo enviar la notificación: {}", e.getMessage());
        }
    }
}
