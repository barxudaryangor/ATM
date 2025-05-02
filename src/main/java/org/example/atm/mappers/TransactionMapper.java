package org.example.atm.mappers;

import org.example.atm.dtos.TransactionDTO;
import org.example.atm.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    TransactionDTO transactionToDTO(Transaction transaction);

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    Transaction dtoToTransaction(TransactionDTO transactionDTO);
}
