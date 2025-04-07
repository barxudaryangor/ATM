package org.example.atm.Bank;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankMapper {
    BankDTO bankToDTO(Bank bank);
    Bank dtoToBank(BankDTO bankDTO);

    List<BankDTO> bankListToDTO(List<Bank> banks);
    List<Bank> dtoListToBank(List<BankDTO> dtoBanks);

}
