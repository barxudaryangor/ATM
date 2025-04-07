package org.example.atm.BankAccount;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = BankAccountMapperHelper.class)
public interface BankAccountMapper {

    @Mapping(source = "human.id" ,target = "humanId" )
    BankAccountDTO bankAccountToDTO(BankAccount bankAccount);

    @Mapping(source = "humanId", target = "human", qualifiedByName = "getHumanById")
    BankAccount dtoToBankAccount(BankAccountDTO bankAccountDTO);


    List<BankAccountDTO> bankAccountListToDTO(List<BankAccount> bankAccounts);


    List<BankAccount> dtoListToBankAccount(List<BankAccountDTO> bankAccountDTOs);
}
