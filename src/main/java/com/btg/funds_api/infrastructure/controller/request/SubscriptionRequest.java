package com.btg.funds_api.infrastructure.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
    @NotBlank(message = "El id del fondo es obligatorio")
    private String fundId;
}
