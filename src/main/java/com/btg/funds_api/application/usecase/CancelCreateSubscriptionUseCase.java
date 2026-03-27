package com.btg.funds_api.application.usecase;

import com.btg.funds_api.domain.exception.TransactionException;
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
public class CancelCreateSubscriptionUseCase {

    private final TransactionGateway transactionGateway;
    private final UserGateway userGateway;
    private final FundGateway fundGateway;
    private final NotificationGateway notificationGateway;

    public void execute(String userId, String fundId) {
        User user = userGateway.getById(userId);

        Fund fund = fundGateway.getById(fundId);

        Optional<Transaction> lastTransaction = transactionGateway.getByUserIdAndFundId(
                user.getId(), fund.getId()
        );

        if (lastTransaction.isEmpty() || lastTransaction.get().getType() != TransactionType.OPEN) {
            throw new TransactionException(TransactionException.CANCELLATION_NOT_ALLOWED);
        }

        Transaction cancellation = Transaction.builder()
                .userId(user.getId())
                .fundId(fund.getId())
                .fundName(fund.getName())
                .amount(lastTransaction.get().getAmount())
                .type(TransactionType.CANCEL)
                .date(LocalDateTime.now())
                .build();

        transactionGateway.save(cancellation);

        user.setBalance(user.getBalance() + lastTransaction.get().getAmount());

        userGateway.update(user);

        try {
            String message = String.format(
                    NotificationGateway.CANCELLATION_MESSAGE,
                    fund.getName(), fund.getMinAmount());
            notificationGateway.send(user, fund, message);
        } catch (Exception e) {
            log.warn("No se pudo enviar la notificación: {}", e.getMessage());
        }
    }
}
