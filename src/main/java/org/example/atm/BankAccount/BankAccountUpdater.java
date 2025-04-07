package org.example.atm.BankAccount;

import org.example.atm.Human.Human;
import org.example.atm.Human.HumanMapper;
import org.example.atm.Human.HumanService;
import org.springframework.stereotype.Component;


@Component
public class BankAccountUpdater {
    private final HumanMapper humanMapper;
    private final HumanService humanService;

    public BankAccountUpdater(HumanMapper humanMapper, HumanService humanService) {
        this.humanMapper = humanMapper;
        this.humanService = humanService;
    }

    public void updateBankAccount(BankAccount bankAccount, BankAccountDTO bankAccountDTO) {

        if(bankAccountDTO.getHumanId() != null) {
            Human human = humanMapper.dtoToHuman(humanService.getHumanById(bankAccountDTO.getHumanId()));
            bankAccount.setHuman(human);
        }

        bankAccount.setAccount_num(bankAccountDTO.getAccount_num());
        bankAccount.setBalance(bankAccountDTO.getBalance());




    }
}
