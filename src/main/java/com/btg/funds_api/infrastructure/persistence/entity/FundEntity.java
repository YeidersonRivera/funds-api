package com.btg.funds_api.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "funds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundEntity {
    @Id
    private String id;
    private String name;
    private Double minAmount;
    private String category;
}
