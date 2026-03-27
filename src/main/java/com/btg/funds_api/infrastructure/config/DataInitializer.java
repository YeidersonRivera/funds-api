package com.btg.funds_api.infrastructure.config;

import com.btg.funds_api.infrastructure.persistence.FundRepository;
import com.btg.funds_api.infrastructure.persistence.entity.FundEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final FundRepository fundRepository;

    private static final List<FundEntity> BTG_FUNDS = List.of(
            FundEntity.builder().id("1").name("FPV_BTG_PACTUAL_RECAUDADORA")
                    .minAmount(75000.0).category("FPV").build(),
            FundEntity.builder().id("2").name("FPV_BTG_PACTUAL_ECOPETROL")
                    .minAmount(125000.0).category("FPV").build(),
            FundEntity.builder().id("3").name("DEUDAPRIVADA")
                    .minAmount(50000.0).category("FIC").build(),
            FundEntity.builder().id("4").name("FDO-ACCIONES")
                    .minAmount(250000.0).category("FIC").build(),
            FundEntity.builder().id("5").name("FPV_BTG_PACTUAL_DINAMICA")
                    .minAmount(100000.0).category("FPV").build()
    );

    @Override
    public void run(@NonNull ApplicationArguments args) {
        BTG_FUNDS.forEach(fund -> {
            if (!fundRepository.existsById(fund.getId())) {
                fundRepository.save(fund);
                log.info("Fondo creado: {}", fund.getName());
            }
        });
        log.info("Fondos inicializados correctamente");
    }
}
