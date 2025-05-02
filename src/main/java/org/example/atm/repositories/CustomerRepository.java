package org.example.atm.repositories;

import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Customer;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.services.BankAccountServiceImpl;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountServiceImpl bankAccountService;

    public void updateCustomer(Customer customer, CustomerDTO customerDTO) {
        if (customerDTO.getBankAccounts() != null) {
            List<Long> bankAccountsIds = customerDTO.getBankAccounts().stream()
                    .map(BankAccountShortDTO::id)
                    .collect(Collectors.toList());
            List<BankAccount> bankAccounts = bankAccountJpaRepository.findAllById(bankAccountsIds);
            customer.setBankAccounts(bankAccounts);
        }

        customer.setBirthDate(customerDTO.getBirthDate());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setBirthDate(customerDTO.getBirthDate());
    }

    public List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId) {
        List<BankAccount> bankAccounts = bankAccountJpaRepository.findAllByCustomerId(customerId);
        return bankAccounts.stream().map(bankAccountService::bankAccountToDTO)
                .toList();
    }

}
