package org.example.atm.controllers;

import jakarta.validation.Valid;
import org.example.atm.responses.TransactionPaginationResponse;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.dtos.TransactionResponse;
import org.example.atm.dtos.TransferRequest;
import org.example.atm.enums.TransactionType;
import org.example.atm.services.TransactionServiceImpl;
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

    @GetMapping("/filter")
    ResponseEntity<TransactionResponse> getTransactionsWithPagination(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "0") int pageSize
    ) {
        return ResponseEntity.ok(transactionService.getTransactionsWithPagination(pageNum, pageSize));
    }

    @GetMapping("/filter/spec")

    ResponseEntity<TransactionPaginationResponse> getTransactionsWithFilter(
            @RequestParam(required = false) Long senderId,
            @RequestParam(required = false) Long receiverId,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "0") int pageSize
    ) {
        return ResponseEntity.ok(transactionService.getTransactionsWithFilter(
                senderId, receiverId, transactionType, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
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
