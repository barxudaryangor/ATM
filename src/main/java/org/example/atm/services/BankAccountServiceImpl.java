package org.example.atm.services;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.entities.Bank;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Transaction;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.BankJpaRepository;
import org.example.atm.jpa_repositories.CustomerJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.example.atm.mappers.BankAccountMapper;
import org.example.atm.mappers.BankMapper;
import org.example.atm.mappers.CustomerMapper;
import org.example.atm.mappers.TransactionMapper;
import org.example.atm.repositories.BankAccountRepository;
import org.example.atm.responses.BankAccountPaginationResponse;
import org.example.atm.services_interfaces.BankAccountService;
import org.example.atm.short_dtos.BankShortDTO;
import org.example.atm.short_dtos.CustomerShortDTO;
import org.example.atm.short_dtos.TransactionShortDTO;
import org.example.atm.specification.BankAccountSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final BankJpaRepository bankJpaRepository;
    private final BankMapper bankMapper;
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerMapper customerMapper;
    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionMapper transactionMapper;



    public BankAccountServiceImpl(BankAccountJpaRepository bankAccountJpaRepository, BankAccountMapper bankAccountMapper, BankAccountRepository bankAccountRepository, BankJpaRepository bankJpaRepository, BankMapper bankMapper, CustomerJpaRepository customerJpaRepository, CustomerMapper customerMapper, TransactionJpaRepository transactionJpaRepository, TransactionMapper transactionMapper) {
        this.bankAccountJpaRepository = bankAccountJpaRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.bankJpaRepository = bankJpaRepository;
        this.bankMapper = bankMapper;
        this.customerJpaRepository = customerJpaRepository;
        this.customerMapper = customerMapper;

        this.transactionJpaRepository = transactionJpaRepository;
        this.transactionMapper = transactionMapper;
    }


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
                            transaction.getSender() != null ? transaction.getSender().getAccountNum() : null,
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

    public BankAccount dtoToBankAccount(BankAccountDTO bankAccountDTO) {
        if ( bankAccountDTO == null ) {
            return null;
        }

        BankAccount bankAccount = bankAccountMapper.dtoToBankAccount(bankAccountDTO);

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

        if(bankAccountDTO.getSentTransactions() != null) {
            List<Long> sentTransactionsIds = bankAccountDTO.getSentTransactions().stream()
                    .map(TransactionShortDTO::id).toList();
            List<Transaction> sentTransactions = transactionJpaRepository.findAllById(sentTransactionsIds);
            bankAccount.setSentTransactions(sentTransactions);
        }

        if(bankAccountDTO.getReceivedTransactions() != null) {
            List<Long> receivedTransactionIds = bankAccountDTO.getReceivedTransactions().stream()
                    .map(TransactionShortDTO::id).toList();
            List<Transaction> receivedTransactions = transactionJpaRepository.findAllById(receivedTransactionIds);
            bankAccount.setReceivedTransactions(receivedTransactions);
        }


        return bankAccount;
    }

    @Override
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountJpaRepository.findAll().stream()
                .map(this::bankAccountToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccountById(Long id) {
        BankAccount bankAccount = bankAccountJpaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("bank.account.not.found"));

        return bankAccountToDTO(bankAccount);
    }



    @Override
    public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = dtoToBankAccount(bankAccountDTO);
        return bankAccountToDTO(bankAccountJpaRepository.save(bankAccount));
    }

    @Override
    public BankAccountDTO updateBankAccount(Long id, BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountJpaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("bank.account.not.found"));

        bankAccountRepository.updateBankAccount(bankAccount, bankAccountDTO);
        return bankAccountToDTO(bankAccountJpaRepository.save(bankAccount));
    }

    @Override
    public void deleteBankAccount(Long id) {
        BankAccount bankAccount = bankAccountJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("bank.account.not.found"));
        bankAccountJpaRepository.delete(bankAccount);
    }

    @Override
    public BankAccountPaginationResponse getBankAccountsWithFilter(String account_num, Long customerId, Long bankId, int pageNum, int pageSize) {
        return bankAccountRepository.getBankAccountsWithFilter(
                account_num, customerId, bankId, pageNum, pageSize);
    }
}
