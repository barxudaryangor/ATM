package org.example.atm.repositories;

import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.entities.Bank;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Transaction;
import org.example.atm.jpa_repositories.BankJpaRepository;
import org.example.atm.jpa_repositories.CustomerJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.example.atm.short_dtos.TransactionShortDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BankAccountRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final BankJpaRepository bankJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;

    public void updateBankAccount(BankAccount bankAccount, BankAccountDTO bankAccountDTO) {
        if(bankAccountDTO.getBank() != null) {
            var bank = bankJpaRepository.findById(bankAccountDTO.getBank().id())
                    .orElseThrow(() -> new RuntimeException("bank.not.found"));

            bankAccount.setBank(bank);
        }

        if(bankAccountDTO.getCustomer() != null) {
            var customer = customerJpaRepository.findById(bankAccountDTO.getCustomer().id())
                    .orElseThrow(() -> new RuntimeException("customer.not.found"));

            bankAccount.setCustomer(customer);
        }

        if(bankAccountDTO.getReceivedTransactions() != null) {
            List<Long> receivedTransactionsIds = bankAccountDTO.getReceivedTransactions().stream()
                    .map(TransactionShortDTO::id).toList();
            List<Transaction> receivedTransactions = transactionJpaRepository.findAllById(receivedTransactionsIds);
            bankAccount.setReceivedTransactions(receivedTransactions);
        }

        if(bankAccountDTO.getSentTransactions() != null) {
            List<Long> sentTransactionsIds = bankAccountDTO.getSentTransactions().stream()
                    .map(TransactionShortDTO::id).toList();
            List<Transaction> sentTransactions = transactionJpaRepository.findAllById(sentTransactionsIds);
            bankAccount.setSentTransactions(sentTransactions);
        }

        bankAccount.setAccount_num(bankAccount.getAccount_num());
        bankAccount.setBalance(bankAccount.getBalance());
    }
}
