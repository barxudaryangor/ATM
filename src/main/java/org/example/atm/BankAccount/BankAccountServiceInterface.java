package org.example.atm.BankAccount;

import org.example.atm.Human.Human;

import java.util.List;

public interface BankAccountServiceInterface {
    String transferMoney(Human sender, Human receiver, double amount);
    List<BankAccountDTO> getAllBankAccounts();
    BankAccountDTO getBankAccountById(Long id);
    BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO);
    BankAccountDTO updateBankAccount(Long id, BankAccountDTO bankAccountDTO);
    void deleteBankAccount(Long id);
}
