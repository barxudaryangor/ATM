package org.example.atm.repositories;

import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Customer;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.CustomerJpaRepository;
import org.example.atm.mappers.CustomerMapper;
import org.example.atm.responses.CustomerPaginationResponse;
import org.example.atm.services.BankAccountServiceImpl;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountServiceImpl bankAccountService;
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerMapper customerMapper;


    public CustomerDTO customerToDTO(Customer customer) {
        if (customer == null) {
            throw new RuntimeException("customer.not.found");
        }

        CustomerDTO customerDTO = customerMapper.customerToDTO(customer);

        if (customer.getBankAccounts() != null) {
            List<BankAccountShortDTO> bankAccountsShortDTOs = customer.getBankAccounts().stream()
                    .map(bankAccount -> new BankAccountShortDTO(bankAccount.getId(), bankAccount.getAccountNum() , bankAccount.getBalance()))
                    .collect(Collectors.toList());
            customerDTO.setBankAccounts(bankAccountsShortDTOs);
        }

        return customerDTO;
    }

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

    public CustomerPaginationResponse getCustomersWithFilter(String firstName, String lastName, LocalDate birthDate, int pageNum, int pageSize) {
        Page<Customer> page = customerJpaRepository.getCustomersWithFilter(
                firstName, lastName, birthDate, pageNum, pageSize);

        Page<CustomerDTO> pageDTO = page.map(this::customerToDTO);
        return new CustomerPaginationResponse(pageDTO);
    }
}
