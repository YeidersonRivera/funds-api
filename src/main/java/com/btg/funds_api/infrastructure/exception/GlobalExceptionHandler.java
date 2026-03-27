package com.btg.funds_api.infrastructure.exception;

import com.btg.funds_api.domain.exception.FundException;
import com.btg.funds_api.domain.exception.TransactionException;
import com.btg.funds_api.domain.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FundException.class)
    public ResponseEntity<String> handleFundException(FundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        HttpStatus status = ex.getMessage().contains(UserException.INSUFFICIENT_BALANCE)
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> handleTransactionException(TransactionException ex) {
        HttpStatus status;
        if (ex.getMessage().contains(TransactionException.ALREADY_SUBSCRIBED)) {
            status = HttpStatus.CONFLICT;
        } else if (ex.getMessage().contains(TransactionException.CANCELLATION_NOT_ALLOWED)) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("No tiene permisos para realizar esta acción");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido o expirado");
    }
}
