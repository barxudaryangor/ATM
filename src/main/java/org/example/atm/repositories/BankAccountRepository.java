package org.example.atm.repositories;

import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.entities.Bank;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Transaction;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.BankJpaRepository;
import org.example.atm.jpa_repositories.CustomerJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.example.atm.mappers.BankAccountMapper;
import org.example.atm.responses.BankAccountPaginationResponse;
import org.example.atm.short_dtos.BankShortDTO;
import org.example.atm.short_dtos.CustomerShortDTO;
import org.example.atm.short_dtos.TransactionShortDTO;
import org.example.atm.specification.BankAccountSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BankAccountRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final BankJpaRepository bankJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountDTO bankAccountToDTO(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }

        BankAccountDTO bankAccountDTO = bankAccountMapper.bankAccountToDTO(bankAccount);

        if (bankAccount.getBank() != null) {
            var bank = bankJpaRepository.findById(bankAccount.getBank().getId())
                    .orElseThrow(() -> new RuntimeException("bank.not.found"));
            BankShortDTO bankShortDTO = new BankShortDTO(
                    bank.getId(),
                    bank.getName(),
                    bank.getLocation()
            );
            bankAccountDTO.setBank(bankShortDTO);
        }

        if(bankAccount.getCustomer() !=null) {
            var customer = customerJpaRepository.findById(bankAccount.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("customer.not.found"));
            CustomerShortDTO customerShortDTO = new CustomerShortDTO(
                    customer.getId(),
                    customer.getFirstName(),
                    customer.getLastName()
            );

            bankAccountDTO.setCustomer(customerShortDTO);
        }

        if(bankAccount.getSentTransactions() != null) {
            List<TransactionShortDTO> sentTransactionDTOs = bankAccount.getSentTransactions()
                    .stream().map(transaction -> new TransactionShortDTO(
                            transaction.getId(),
                            transaction.getSender() != null ? transaction.getSender().getAccountNum()  : null,
                            transaction.getReceiver() != null ? transaction.getReceiver().getAccountNum() : null
                    )).toList();
            bankAccountDTO.setSentTransactions(sentTransactionDTOs);
        }

        if (bankAccount.getReceivedTransactions() != null) {
            List<TransactionShortDTO> receivedTransactionDTOs = bankAccount.getReceivedTransactions()
                    .stream().map(transaction -> new TransactionShortDTO(
                            transaction.getId(),
                            transaction.getSender() != null ? transaction.getSender().getAccountNum()  : null,
                            transaction.getReceiver() != null ? transaction.getReceiver().getAccountNum()  : null
                    )).toList();
            bankAccountDTO.setReceivedTransactions(receivedTransactionDTOs);
        }


        return bankAccountDTO;
    }

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

        bankAccount.setAccountNum(bankAccount.getAccountNum());
        bankAccount.setBalance(bankAccount.getBalance());
    }

    public BankAccountPaginationResponse getBankAccountsWithFilter(String account_num, Long customerId, Long bankId, int pageNum, int pageSize) {
        Specification<BankAccount> spec = Specification.where(null);

        if(account_num != null && !account_num.isBlank()) {
            spec = spec.and(BankAccountSpecification.hasAccountNum(account_num));
        }

        if(customerId != null) {
            spec = spec.and(BankAccountSpecification.hasCustomerId(customerId));
        }

        if(bankId != null) {
            spec = spec.and(BankAccountSpecification.hasBankId(bankId));
        }

        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<BankAccount> page = bankAccountJpaRepository.findAll(spec,pageable);
        Page<BankAccountDTO> pageDTO = page.map(this::bankAccountToDTO);

        return new BankAccountPaginationResponse(pageDTO);
    }
}
