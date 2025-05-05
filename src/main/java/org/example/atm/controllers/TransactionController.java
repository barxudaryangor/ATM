package org.example.atm.controllers;

import jakarta.validation.Valid;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.dtos.TransferRequest;
import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.example.atm.services.TransactionServiceImpl;
import org.example.atm.specification.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm/transactions")
@Validated
public class TransactionController {
    private final TransactionServiceImpl transactionService;

    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/filter")
    ResponseEntity<Page<TransactionDTO>> getFilteredTransactions(
            @RequestParam(required = false) Long senderId,
            @RequestParam(required = false) Long receiverId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Specification<Transaction> spec = Specification.where(null);

        if (senderId != null) {
            spec = spec.and(TransactionSpecification.hasReceiverId(senderId));
        }
        if (receiverId != null) {
            spec = spec.and(TransactionSpecification.hasReceiverId(receiverId));
        }

        if (type != null) {
            spec = spec.and(TransactionSpecification.hasType(type));
        }

        return ResponseEntity.ok(transactionService.getTransactionsWithFilter(spec, PageRequest.of(page,size)));
    }

    @PostMapping
    ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.status(201).body(transactionService.createTransaction(transactionDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id,transactionDTO));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    ResponseEntity<String> transfer (@RequestBody @Valid TransferRequest request)
    {
        try {
            transactionService.transfer(
                    request.getSenderId(),
                    request.getReceiverId(),
                    request.getAmount(),
                    request.getTransactionType()
            );
            return ResponseEntity.ok("transfer.successful");
        } catch(RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
