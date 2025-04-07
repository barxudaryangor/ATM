package org.example.atm.BankAccount;

import org.example.atm.Bank.BankMapper;
import org.example.atm.Human.Human;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountService implements BankAccountServiceInterface {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountUpdater bankAccountUpdater;

    public BankAccountService(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper, BankAccountUpdater bankAccountUpdater) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountUpdater = bankAccountUpdater;
    }

    public String transferMoney(Human sender, Human receiver, double amount) {
        BankAccount senderAccount = bankAccountRepository.findByHuman(sender)
                .orElseThrow(() -> new RuntimeException("Счёт отправителя не найден"));

        BankAccount receiverAccount = bankAccountRepository.findByHuman(receiver)
                .orElseThrow(() -> new RuntimeException("Счет получателя не найден"));

        if (senderAccount.getBalance() < amount) {
            return "Недостаточно средств на счёте!";
        }

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(receiverAccount);

        return "Перевод выполнен успешно!";
    }

    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::bankAccountToDTO)
                .collect(Collectors.toList());
    }

    public BankAccountDTO getBankAccountById(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BankAccount not found"));

        return bankAccountMapper.bankAccountToDTO(bankAccount);
    }

    public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountMapper.dtoToBankAccount(bankAccountDTO);
        return bankAccountMapper.bankAccountToDTO(bankAccountRepository.save(bankAccount));
    }

    public BankAccountDTO updateBankAccount(Long id, BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BankAccount not found"));

        bankAccountUpdater.updateBankAccount(bankAccount, bankAccountDTO);
        return bankAccountMapper.bankAccountToDTO(bankAccountRepository.save(bankAccount));
    }

    public void deleteBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BankAccount not found"));

        bankAccountRepository.delete(bankAccount);
    }


}
