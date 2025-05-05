package org.example.atm.services;

import org.example.atm.entities.ExchangeRate;
import org.example.atm.jpa_repositories.ExchangeRateJpaRepository;
import org.example.atm.repositories.ExchangeRateRepository;
import org.example.atm.services_interfaces.ExchangeRateService;
import org.example.atm.short_dtos.ExchangeRateShortDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
            System.out.println(">>> Scheduler triggered: " + LocalDateTime.now());
            Double rate = exchangeRateRepository.fetchUsdToAmdRateFromApi();
            ExchangeRate exchangeRate = new ExchangeRate(null, rate, LocalDateTime.now());
            exchangeRateJpaRepository.save(exchangeRate);
            System.out.println("Saved rate: " + rate);
        } catch (Exception e) {
            System.err.println("Error fetching rate: " + e.getMessage());
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

}
