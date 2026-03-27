package com.btg.funds_api.infrastructure.persistence.entity;

import com.btg.funds_api.domain.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {
    @Id
    private String id;
    private String userId;
    private String fundId;
    private String fundName;
    private Double amount;
    private TransactionType type;
    private LocalDateTime date;
}
