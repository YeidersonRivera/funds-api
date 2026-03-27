package com.btg.funds_api.domain.exception;

public class TransactionException extends RuntimeException {
    public static final String ALREADY_SUBSCRIBED = "Ya tiene una suscripción activa al fondo ";
    public static final String NOT_FOUND = "Suscripción no encontrada";
    public static final String CANCELLATION_NOT_ALLOWED = "No es posible cancelar la suscripción al fondo ";
    public TransactionException(String message) {
        super(message);
    }
}
