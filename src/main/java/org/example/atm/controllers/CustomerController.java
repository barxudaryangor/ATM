package org.example.atm.controllers;

import jakarta.validation.Valid;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.responses.CustomerPaginationResponse;
import org.example.atm.services.CustomerServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/atm/customers")
@Validated
public class CustomerController {
    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    ResponseEntity<CustomerPaginationResponse> getCustomersWithFilter(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        return ResponseEntity.ok(customerService.getCustomersWithFilter(
                firstName, lastName, birthDate, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/{id}/bank_accounts")
    ResponseEntity<List<BankAccountDTO>> getBankAccountsByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getBankAccountsByCustomerId(id));
    }
    @PostMapping
    ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.status(201).body(customerService.createCustomer(customerDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(customerService.updateCustomer(id,customerDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
