package com.btg.funds_api.infrastructure.persistence;

import com.btg.funds_api.infrastructure.persistence.entity.FundEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends MongoRepository<FundEntity, String> {
}
