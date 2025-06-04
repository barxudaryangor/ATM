package org.example.atm.services;

import lombok.extern.slf4j.Slf4j;
import org.example.atm.entities.ExchangeRate;
import org.example.atm.jpa_repositories.ExchangeRateJpaRepository;
import org.example.atm.repositories.ExchangeRateRepository;
import org.example.atm.responses.ExchangeRatePaginationResponse;
import org.example.atm.services_interfaces.ExchangeRateService;
import org.example.atm.short_dtos.ExchangeRateShortDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateJpaRepository exchangeRateJpaRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateServiceImpl(ExchangeRateJpaRepository exchangeRateJpaRepository, ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateJpaRepository = exchangeRateJpaRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public ExchangeRateShortDTO exchangeRateToDTO(ExchangeRate exchangeRate) {
        return new ExchangeRateShortDTO(exchangeRate.getUsdToAmd(),exchangeRate.getLocalDateTime());
    }

    @Override
    public List<ExchangeRateShortDTO> getAllExchangeRates() {

        return exchangeRateJpaRepository.findAll().stream()
                .map(this::exchangeRateToDTO).toList();
    }

    @Override
    @Scheduled(cron = "0 0 */6 * * *")
    public void fetchUsdToAmdRate() {
        try {
            log.info(">>> Scheduler triggered at {}", LocalDateTime.now());
            Double rate = exchangeRateRepository.fetchUsdToAmdRateFromApi();
            ExchangeRate exchangeRate = new ExchangeRate(rate, LocalDateTime.now());
            exchangeRateJpaRepository.save(exchangeRate);
            log.info("Saved USD to AMD rate: {}", rate);
        } catch (Exception e) {
            log.error("Error fetching exchange rate: {}", e.getMessage(), e);
        }
    }

    @Override
    public ExchangeRateShortDTO getLatestRate() {
        ExchangeRate latest = exchangeRateJpaRepository.findTopByOrderByLocalDateTimeDesc();

        if (latest == null) {
            throw new RuntimeException("no.exchange.rate.available.in.the.database");
        }

        return new ExchangeRateShortDTO(latest.getUsdToAmd(), latest.getLocalDateTime());
    }

    @Override
    public ExchangeRatePaginationResponse getRatesWithFilter(Double usdToAmd, LocalDateTime localDateTime, int pageNum, int pageSize) {
        return exchangeRateRepository.getRatesWithFilter(
                usdToAmd, localDateTime, pageNum, pageSize);
    }

}
