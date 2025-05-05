package org.example.atm.services_interfaces;

import org.example.atm.short_dtos.ExchangeRateShortDTO;

import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRateShortDTO> getAllExchangeRates();
    void fetchUsdToAmdRate();
    ExchangeRateShortDTO getLatestRate();
}
