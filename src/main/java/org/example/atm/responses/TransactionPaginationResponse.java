package org.example.atm.responses;

import org.example.atm.dtos.TransactionDTO;
import org.springframework.data.domain.Page;

public class TransactionPaginationResponse extends AbstractPaginationResponse<TransactionDTO> {

    public TransactionPaginationResponse(Page<TransactionDTO> page) {
        super(page);
    }
}
