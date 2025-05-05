package org.example.atm.short_dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExchangeRateShortDTO(
        Double usdToAmd,
        LocalDateTime localDateTime
) {

}
