package com.btg.funds_api.infrastructure.controller;

import com.btg.funds_api.application.usecase.GetTransactionsUseCase;
import com.btg.funds_api.infrastructure.controller.response.TransactionResponse;
import com.btg.funds_api.infrastructure.mapper.TransactionMapper;
import com.btg.funds_api.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final GetTransactionsUseCase getTransactionsUseCase;
    private final TransactionMapper transactionMapper;
    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        String userId = jwtService.extractId(authHeader.substring(7));
        return ResponseEntity.ok(transactionMapper.toResponseList(getTransactionsUseCase.execute(userId)));
    }
}
