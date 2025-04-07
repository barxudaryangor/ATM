package org.example.atm.Bank;

import org.example.atm.Human.Human;
import org.example.atm.Human.HumanMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankUpdater {

    private final HumanMapper humanMapper;

    public BankUpdater(HumanMapper humanMapper) {
        this.humanMapper = humanMapper;
    }

    public void updateBank (Bank bank, BankDTO bankDTO) {
        bank.setName(bankDTO.getName());
        bank.setLocation(bankDTO.getLocation());

        List<Human> humans = humanMapper.dtoListToHumanList(bankDTO.getHumans());
        bank.setHumans(humans);
    }
}
