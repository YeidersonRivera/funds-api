package com.btg.funds_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private String id;
    private String userId;
    private String fundId;
    private String fundName;
    private Double amount;
    private TransactionType type;
    private LocalDateTime date;
}
