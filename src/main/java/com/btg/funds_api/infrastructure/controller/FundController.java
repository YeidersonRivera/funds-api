package com.btg.funds_api.infrastructure.controller;

import com.btg.funds_api.application.usecase.GetFundsUseCase;
import com.btg.funds_api.infrastructure.controller.response.FundResponse;
import com.btg.funds_api.infrastructure.mapper.FundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/funds")
@RequiredArgsConstructor
public class FundController {
    private final GetFundsUseCase getFundsUseCase;
    private final FundMapper fundMapper;
    @GetMapping
    public ResponseEntity<List<FundResponse>> getAll() {
        return ResponseEntity.ok(fundMapper.toResponseList(getFundsUseCase.execute()));
    }
}
