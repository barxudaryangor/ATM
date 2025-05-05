package org.example.atm.controllers;

import org.example.atm.services_interfaces.ExchangeRateService;
import org.example.atm.short_dtos.ExchangeRateShortDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/atm/exchange-rate")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRateShortDTO>> getAllExchangeRates() {
        return ResponseEntity.ok(exchangeRateService.getAllExchangeRates());
    }

    @GetMapping("/latest")
    public ResponseEntity<ExchangeRateShortDTO> getLatestRate() {
        return ResponseEntity.ok(exchangeRateService.getLatestRate());
    }
}
