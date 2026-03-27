package com.btg.funds_api.domain.exception;

public class FundException extends RuntimeException {
    public static final String NOT_FOUND = "Fondo no encontrado";

    public FundException(String message) {
        super(message);
    }
}
