package com.btg.funds_api.infrastructure.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private String id;
    private String fundName;
    private Double amount;
    private String type;
    private LocalDateTime date;
}
