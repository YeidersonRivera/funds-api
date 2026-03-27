package com.btg.funds_api.infrastructure.controller;

import com.btg.funds_api.application.usecase.CancelCreateSubscriptionUseCase;
import com.btg.funds_api.application.usecase.CreateSubscriptionUseCase;
import com.btg.funds_api.infrastructure.controller.request.SubscriptionRequest;
import com.btg.funds_api.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final CreateSubscriptionUseCase createSubscriptionUseCase;
    private final CancelCreateSubscriptionUseCase cancelCreateSubscriptionUseCase;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Void> subscribe(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SubscriptionRequest subscriptionRequest
    ) {
        String userId = jwtService.extractId(authHeader.substring(7));
        createSubscriptionUseCase.execute(userId, subscriptionRequest.getFundId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SubscriptionRequest subscriptionRequest
    ) {
        String userId = jwtService.extractId(authHeader.substring(7));
        cancelCreateSubscriptionUseCase.execute(userId, subscriptionRequest.getFundId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
