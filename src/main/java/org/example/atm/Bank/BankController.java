package org.example.atm.Bank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm/banks")
public class BankController {
    private final BankServiceInterface bankServiceInterface;

    public BankController(BankServiceInterface bankServiceInterface) {
        this.bankServiceInterface = bankServiceInterface;
    }

    @GetMapping
    public ResponseEntity<List<BankDTO>> getAllBanks() {
        return ResponseEntity.ok(bankServiceInterface.getAllBanks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDTO> getBankById(@PathVariable Long id) {
        return ResponseEntity.ok(bankServiceInterface.getBankById(id));
    }

    @PostMapping
    public ResponseEntity<BankDTO> createBank(@RequestBody BankDTO bankDTO) {
        return ResponseEntity.status(201).body(bankServiceInterface.createBank(bankDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankDTO> updateBank(@PathVariable Long id, @RequestBody BankDTO bankDTO) {
        return ResponseEntity.ok(bankServiceInterface.updateBank(id, bankDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBank(@PathVariable Long id) {
        bankServiceInterface.deleteBank(id);
        return ResponseEntity.noContent().build();
    }

}
