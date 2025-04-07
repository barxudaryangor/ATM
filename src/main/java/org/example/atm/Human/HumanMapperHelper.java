package org.example.atm.Human;

import org.example.atm.Bank.Bank;
import org.example.atm.Bank.BankRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class HumanMapperHelper {
    private final BankRepository bankRepository;

    public HumanMapperHelper(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Named("getBankById")
    public Bank getBankById(Long bankId) {
        if(bankId == null) {
            return null;
        }


        return bankRepository.findById(bankId).orElseThrow(() -> new RuntimeException("Bank not found"));
    }

}
