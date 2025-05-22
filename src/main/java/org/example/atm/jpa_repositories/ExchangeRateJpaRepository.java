package org.example.atm.jpa_repositories;

import org.example.atm.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateJpaRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findTopByOrderByLocalDateTimeDesc();
}
