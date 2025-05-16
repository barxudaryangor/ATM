package org.example.atm.repositories;

import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.Bank;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Customer;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.BankJpaRepository;
import org.example.atm.jpa_repositories.CustomerJpaRepository;
import org.example.atm.mappers.BankAccountMapper;
import org.example.atm.services.BankAccountServiceImpl;
import org.example.atm.services.BankServiceImpl;
import org.example.atm.services.CustomerServiceImpl;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class BankRepository {
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final CustomerJpaRepository customerJpaRepository;
    private final BankAccountServiceImpl bankAccountService;
    private final CustomerServiceImpl customerService;

    public void updateBank(Bank bank, BankDTO bankDTO) {

        if(bankDTO.getBankAccounts() != null) {
            List<Long> bankAccountsIds = bankDTO.getBankAccounts().stream().map(BankAccountShortDTO::id).toList();
            List<BankAccount> bankAccounts = bankAccountJpaRepository.findAllById(bankAccountsIds);
            bank.setBankAccounts(bankAccounts);
        }
        bank.setName(bankDTO.getName());
        bank.setLocation(bankDTO.getLocation());
    }

    public List<BankAccountDTO> getBankAccountsByBankId(Long bankId) {
        List<BankAccount> bankAccounts = bankAccountJpaRepository.findAllByBankId(bankId);
        return bankAccounts.stream().map(bankAccountService::bankAccountToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerDTO> getAllCustomersByBankId(Long bankId) {
        List<Customer> customers = customerJpaRepository.findAllByBankAccounts_Bank_Id(bankId);
        return customers.stream().map(customerService::customerToDTO)
                .toList();
    }
}


