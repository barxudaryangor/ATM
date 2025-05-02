package org.example.atm.mappers;

import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "bankAccounts", ignore = true)
    CustomerDTO customerToDTO(Customer customer);

    @Mapping(target = "bankAccounts", ignore = true)
    Customer dtoToCustomer(CustomerDTO customerDTO);
}
