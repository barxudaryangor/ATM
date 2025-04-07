package org.example.atm.Human;

import org.example.atm.Bank.BankMapper;
import org.example.atm.BankAccount.BankAccount;
import org.example.atm.BankAccount.BankAccountDTO;
import org.example.atm.BankAccount.BankAccountMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BankMapper.class, BankAccountMapper.class, HumanMapperHelper.class})
public interface HumanMapper {

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bankAccounts", target = "bankAccounts")
    HumanDTO humanToDTO(Human human);

    @Mapping(source = "bankId", target = "bank", qualifiedByName = "getBankById")
    @Mapping(source = "bankAccounts", target = "bankAccounts")
    Human dtoToHuman(HumanDTO humanDTO);

    List<Human> dtoListToHumanList(List<HumanDTO> humanDTOs);
    List<HumanDTO> humanListoToDTOList(List<Human> humans);



}

