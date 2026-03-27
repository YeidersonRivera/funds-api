package com.btg.funds_api.infrastructure.persistence;

import com.btg.funds_api.domain.model.TransactionType;
import com.btg.funds_api.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionEntity, String> {
    List<TransactionEntity> findByUserId(String userId);

    Optional<TransactionEntity> findTopByUserIdAndFundIdOrderByDateDesc(String userId,
                                                                        String fundId);
}
