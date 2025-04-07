package org.example.atm.BankAccount;

import org.example.atm.Bank.Bank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm/bank_accounts")
public class BankAccountController {
    private final BankAccountServiceInterface bankAccountServiceInterface;


    public BankAccountController(BankAccountServiceInterface bankAccountServiceInterface) {
        this.bankAccountServiceInterface = bankAccountServiceInterface;
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts() {
        return ResponseEntity.ok(bankAccountServiceInterface.getAllBankAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountServiceInterface.getBankAccountById(id));
    }

    @PostMapping
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        return ResponseEntity.status(201).body(bankAccountServiceInterface.createBankAccount(bankAccountDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountDTO> updateBankAccount(@PathVariable Long id, @RequestBody BankAccountDTO bankAccountDTO) {
        return ResponseEntity.ok(bankAccountServiceInterface.updateBankAccount(id,bankAccountDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBankAccount(@PathVariable Long id) {
        bankAccountServiceInterface.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }

}
