package org.example.atm.specification;

import org.example.atm.entities.ExchangeRate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ExchangeRateSpecification {

    public static Specification<ExchangeRate> hasExactRate(Double usdToAmd) {
        return (root,query,cb) ->
                cb.equal(root.get("usdToAmd"), usdToAmd);
    }

    public static Specification<ExchangeRate> hasEqualDate(LocalDateTime localDateTime) {
        return (root,query,cb) ->
                cb.equal(root.get("localDateTime"), localDateTime);
    }

}
