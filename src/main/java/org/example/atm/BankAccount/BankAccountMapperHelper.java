package org.example.atm.BankAccount;

import org.example.atm.Human.Human;
import org.example.atm.Human.HumanRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapperHelper {
    private final HumanRepository humanRepository;

    public BankAccountMapperHelper(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    @Named("getHumanById")
    public Human getHumanById(Long humanId) {

        if(humanId == null) {
            return null;
        }

        return humanRepository.findById(humanId).orElseThrow(() -> new RuntimeException("Human not found"));
    }
}
