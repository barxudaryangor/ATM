package org.example.atm.controllers;

import jakarta.validation.Valid;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.responses.BankPaginationResponse;
import org.example.atm.services.BankServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm/banks")
@Validated
public class BankController {
    private final BankServiceImpl bankService;

    public BankController(BankServiceImpl bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/all")
    ResponseEntity<List<BankDTO>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @GetMapping("/{id}")
    ResponseEntity<BankDTO> getBankById(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBankById(id));
    }

    @GetMapping("/{id}/bank_accounts")
    ResponseEntity<List<BankAccountDTO>> getBankAccountsByBankId(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBankAccountsByBankId(id));
    }

    @GetMapping("/{id}/customers")
    ResponseEntity<List<CustomerDTO>> getAllCustomersByBankId(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getAllCustomersByBankId(id));
    }

    @GetMapping()
    ResponseEntity<BankPaginationResponse> getBanksWithFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        return ResponseEntity.ok(bankService.getBanksWithFilter(
                name, location, pageNum, pageSize));
    }

    @PostMapping
    ResponseEntity<BankDTO> createBank(@Valid @RequestBody BankDTO bankDTO) {
        return ResponseEntity.status(201).body(bankService.createBank(bankDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<BankDTO> updateBank(@PathVariable Long id, @Valid @RequestBody BankDTO bankDTO) {
        return ResponseEntity.ok(bankService.updateBank(id,bankDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }
}
