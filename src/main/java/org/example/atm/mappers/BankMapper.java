package org.example.atm.mappers;


import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.entities.Bank;
import org.example.atm.entities.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankMapper {
    @Mapping(target = "bankAccounts", ignore = true)
    BankDTO bankToDTO(Bank bank);

    @Mapping(target = "bankAccounts", ignore = true)
    Bank dtoToBank(BankDTO bankDTO);



}
