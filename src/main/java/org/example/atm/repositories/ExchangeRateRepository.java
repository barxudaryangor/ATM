package org.example.atm.repositories;

import lombok.RequiredArgsConstructor;
import org.example.atm.entities.ExchangeRate;
import org.example.atm.jpa_repositories.ExchangeRateJpaRepository;
import org.example.atm.responses.ExchangeRatePaginationResponse;
import org.example.atm.short_dtos.ExchangeRateShortDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ExchangeRateRepository {
    private final ExchangeRateJpaRepository exchangeRateJpaRepository;
    private final RestTemplate restTemplate = new RestTemplate();


    public ExchangeRateShortDTO exchangeRateToDTO(ExchangeRate exchangeRate) {
        return new ExchangeRateShortDTO(exchangeRate.getUsdToAmd(),exchangeRate.getLocalDateTime());
    }

    public Double fetchUsdToAmdRateFromApi() {
        String url = "https://api.apilayer.com/exchangerates_data/latest?base=USD&symbols=AMD";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "zNkcbGOT445G9tHSFVfNUB1QETntewpe");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        Map<String, Number> rates = (Map<String, Number>) body.get("rates");
        return rates.get("AMD").doubleValue();
    }

    public ExchangeRatePaginationResponse getRatesWithFilter(Double usdToAmd, LocalDateTime localDateTime, int pageNum, int pageSize) {
        Page<ExchangeRate> page = exchangeRateJpaRepository.getRatesWithFilter(
                usdToAmd, localDateTime, pageNum, pageSize);

        Page<ExchangeRateShortDTO> pageDTO = page.map(this::exchangeRateToDTO);
        return new ExchangeRatePaginationResponse(pageDTO);
    }
}
