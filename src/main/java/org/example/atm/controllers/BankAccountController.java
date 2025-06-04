package org.example.atm.controllers;

import jakarta.validation.Valid;
import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.responses.BankAccountPaginationResponse;
import org.example.atm.services.BankAccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("atm/bank_accounts")
@Validated
public class BankAccountController {
    private final BankAccountServiceImpl bankAccountService;

    public BankAccountController(BankAccountServiceImpl bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping()
    ResponseEntity<BankAccountPaginationResponse> getBankAccountsWithFilter(
            @RequestParam(required = false) String accountNum,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long bankId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        return ResponseEntity.ok(bankAccountService.getBankAccountsWithFilter(
                accountNum, customerId, bankId, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    ResponseEntity<BankAccountDTO> getBankAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.getBankAccountById(id));
    }

    @PutMapping
    ResponseEntity<BankAccountDTO> createBankAccount(@Valid @RequestBody BankAccountDTO bankAccountDTO){
        return ResponseEntity.status(201).body(bankAccountService.createBankAccount(bankAccountDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<BankAccountDTO> updateBankAccount(@PathVariable Long id, @Valid @RequestBody BankAccountDTO bankAccountDTO) {
        return ResponseEntity.ok(bankAccountService.updateBankAccount(id,bankAccountDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }
}
