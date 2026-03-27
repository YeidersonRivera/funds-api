package com.btg.funds_api.infrastructure.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FundResponse {
    private String id;
    private String name;
    private Double minAmount;
    private String category;
}
