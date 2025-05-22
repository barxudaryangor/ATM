package org.example.atm.responses;

import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.Customer;
import org.springframework.data.domain.Page;

public class CustomerPaginationResponse extends AbstractPaginationResponse<CustomerDTO> {
    public CustomerPaginationResponse(Page<CustomerDTO> page) {
        super(page);
    }
}
