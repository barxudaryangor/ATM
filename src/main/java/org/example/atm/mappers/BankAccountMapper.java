package org.example.atm.mappers;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.entities.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "bank", ignore = true)
    @Mapping(target = "sentTransactions", ignore = true)
    @Mapping(target = "receivedTransactions", ignore = true)
    BankAccountDTO bankAccountToDTO(BankAccount bankAccount);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "bank", ignore = true)
    @Mapping(target = "sentTransactions", ignore = true)
    @Mapping(target = "receivedTransactions", ignore = true)
    BankAccount dtoToBankAccount(BankAccountDTO bankAccountDTO);
}

