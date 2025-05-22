package org.example.atm.services_interfaces;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.responses.CustomerPaginationResponse;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId);
    CustomerDTO getCustomerById(Long id);
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomer(Long id);
    CustomerPaginationResponse getCustomersWithFilter(String firstName, String LastName, LocalDate birthDate, int pageNum, int pageSize);
}
