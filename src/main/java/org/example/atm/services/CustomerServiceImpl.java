package org.example.atm.services;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Customer;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.CustomerJpaRepository;
import org.example.atm.mappers.BankAccountMapper;
import org.example.atm.mappers.CustomerMapper;
import org.example.atm.repositories.CustomerRepository;
import org.example.atm.responses.CustomerPaginationResponse;
import org.example.atm.services_interfaces.CustomerService;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountMapper bankAccountMapper;

    public CustomerServiceImpl(CustomerJpaRepository customerJpaRepository, CustomerMapper customerMapper, CustomerRepository customerRepository, BankAccountJpaRepository bankAccountJpaRepository, BankAccountMapper bankAccountMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        this.bankAccountJpaRepository = bankAccountJpaRepository;
        this.bankAccountMapper = bankAccountMapper;
    }


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

    public Customer dtoToCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            throw new RuntimeException("customer.not.found");
        }


        Customer customer = customerMapper.dtoToCustomer(customerDTO);


        if (customerDTO.getBankAccounts() != null) {
            List<Long> bankAccountsIds = customerDTO.getBankAccounts().stream()
                    .map(BankAccountShortDTO::id)
                    .collect(Collectors.toList());

            List<BankAccount> bankAccounts = bankAccountJpaRepository.findAllById(bankAccountsIds);
            customer.setBankAccounts(bankAccounts);
        }

        return customer;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerJpaRepository.findAll().stream()
                .map(this::customerToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId) {
        return customerRepository.getBankAccountsByCustomerId(customerId);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("customer.not.found"));
        return customerToDTO(customer);
    }

    @Override
    public CustomerPaginationResponse getCustomersWithFilter(String firstName, String lastName, LocalDate birthDate, int pageNum, int pageSize) {
        return customerRepository.getCustomersWithFilter(
                firstName, lastName, birthDate, pageNum, pageSize);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = dtoToCustomer(customerDTO);
        return customerToDTO(customerJpaRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("customer.not.found"));
        customerRepository.updateCustomer(customer,customerDTO);
        return customerToDTO(customerJpaRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("customer.not.found"));
        customerJpaRepository.delete(customer);
    }


}
