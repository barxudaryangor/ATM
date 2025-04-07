package org.example.atm.Human;

import org.example.atm.Bank.Bank;
import org.example.atm.Bank.BankMapper;
import org.example.atm.Bank.BankRepository;
import org.example.atm.Bank.BankService;
import org.example.atm.BankAccount.BankAccount;
import org.example.atm.BankAccount.BankAccountMapper;
import org.example.atm.BankAccount.BankAccountService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HumanUpdater {
    private final BankMapper bankMapper;
    private final BankService bankService;
    private final HumanMapper humanMapper;
    private final BankAccountMapper bankAccountMapper;

    public HumanUpdater(BankMapper bankMapper, BankService bankService, HumanMapper humanMapper, BankAccountMapper bankAccountMapper) {
        this.bankMapper = bankMapper;
        this.bankService = bankService;
        this.humanMapper = humanMapper;
        this.bankAccountMapper = bankAccountMapper;
    }


    public void updateHuman(Human human, HumanDTO humanDTO) {
        if(humanDTO.getBankId() != null && humanDTO.getBankAccounts() != null) {
            Bank bank = bankMapper.dtoToBank(bankService.getBankById(humanDTO.getBankId()));
            human.setBank(bank);

            List<BankAccount> bankAccounts = bankAccountMapper.dtoListToBankAccount(humanDTO.getBankAccounts());
            human.setBankAccounts(bankAccounts);
        }

        human.setFirst_name(humanDTO.getFirst_name());
        human.setLast_name(humanDTO.getLast_name());
        human.setEmail(humanDTO.getEmail());
        human.setBirth_date(humanDTO.getBirth_date());


    }
}
