package com.btg.funds_api.domain.gateway;

import com.btg.funds_api.domain.model.Fund;
import com.btg.funds_api.domain.model.User;

public interface NotificationGateway {
    String SUBSCRIPTION_MESSAGE = "Tu suscripción al fondo %s fue exitosa. Monto vinculado: $%,.0f";
    String CANCELLATION_MESSAGE = "Tu cancelación del fondo %s fue exitosa. Monto retornado: $%,.0f";

    void send(User user, Fund fund, String message);
}
