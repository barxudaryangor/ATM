package org.example.atm.jpa_repositories;

import org.example.atm.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExchangeRateJpaRepository extends JpaRepository<ExchangeRate, Long>, JpaSpecificationExecutor<ExchangeRate> {
    ExchangeRate findTopByOrderByLocalDateTimeDesc();
}
