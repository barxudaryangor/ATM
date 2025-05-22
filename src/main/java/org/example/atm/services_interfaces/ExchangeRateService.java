package org.example.atm.services_interfaces;

import org.example.atm.responses.ExchangeRatePaginationResponse;
import org.example.atm.short_dtos.ExchangeRateShortDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRateShortDTO> getAllExchangeRates();
    void fetchUsdToAmdRate();
    ExchangeRateShortDTO getLatestRate();
    ExchangeRatePaginationResponse getRatesWithFilter(Double usdToAmd, LocalDateTime localDateTime,int pageNum, int pageSize);
}
