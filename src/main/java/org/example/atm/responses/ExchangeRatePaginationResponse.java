package org.example.atm.responses;

import org.example.atm.short_dtos.ExchangeRateShortDTO;
import org.springframework.data.domain.Page;

public class ExchangeRatePaginationResponse extends AbstractPaginationResponse<ExchangeRateShortDTO> {


    public ExchangeRatePaginationResponse(Page<ExchangeRateShortDTO> page) {
        super(page);
    }
}
