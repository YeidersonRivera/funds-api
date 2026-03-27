package com.btg.funds_api.domain.exception;

public class UserException extends RuntimeException {
    public static final String NOT_FOUND = "Usuario no encontrado";
    public static final String INSUFFICIENT_BALANCE = "No tiene saldo disponible para vincularse al fondo ";
    public UserException (String message) {
        super(message);
    }
}
