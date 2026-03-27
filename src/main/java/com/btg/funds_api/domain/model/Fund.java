package com.btg.funds_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fund {
    private String id;
    private String name;
    private Double minAmount;
    private String category;
}
