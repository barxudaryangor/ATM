package org.example.atm.responses;

import org.example.atm.dtos.BankAccountDTO;
import org.springframework.data.domain.Page;

public class BankAccountPaginationResponse extends AbstractPaginationResponse<BankAccountDTO> {
    public BankAccountPaginationResponse(Page<BankAccountDTO> page) {
        super(page);
    }
}
