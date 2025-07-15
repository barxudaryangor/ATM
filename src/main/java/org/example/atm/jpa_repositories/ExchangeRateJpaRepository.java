package org.example.atm.jpa_repositories;

import org.example.atm.entities.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ExchangeRateJpaRepository extends JpaRepository<ExchangeRate, Long>, JpaSpecificationExecutor<ExchangeRate> {
    ExchangeRate findTopByOrderByLocalDateTimeDesc();

    static Specification<ExchangeRate> hasExactRate(Double usdToAmd) {
        return (root,query,cb) ->
                cb.equal(root.get("usdToAmd"), usdToAmd);
    }

    static Specification<ExchangeRate> hasEqualDate(LocalDateTime localDateTime) {
        return (root,query,cb) ->
                cb.equal(root.get("localDateTime"), localDateTime);
    }

    default Page<ExchangeRate> getRatesWithFilter(Double usdToAmd, LocalDateTime localDateTime, int pageNum, int pageSize) {
        Specification<ExchangeRate> spec = Specification.where(null);

        spec = Optional.ofNullable((usdToAmd))
                .filter(rate-> rate>0)
                .map(ExchangeRateJpaRepository::hasExactRate)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(localDateTime)
                .map(ExchangeRateJpaRepository::hasEqualDate)
                .map(spec::and)
                .orElse(spec);

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return findAll(spec, pageable);
    }
}
