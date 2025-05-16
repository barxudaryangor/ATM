package org.example.atm.responses;

import org.example.atm.dtos.BankDTO;
import org.springframework.data.domain.Page;

public class BankPaginationResponse extends AbstractPaginationResponse<BankDTO> {

    public BankPaginationResponse(Page<BankDTO> page) {
        super(page);
    }
}
