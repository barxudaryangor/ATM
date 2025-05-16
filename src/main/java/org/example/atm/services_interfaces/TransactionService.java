package org.example.atm.services_interfaces;

import org.example.atm.dtos.TransactionDTO;
import org.example.atm.dtos.TransactionResponse;
import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    TransactionDTO getTransactionById(Long id);
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO);
    void deleteTransaction(Long id);
    void transfer(Long senderId, Long receiverId, Double amount, TransactionType transactionType);
    TransactionResponse getTransactionsWithPagination(int pageNum, int pageSize);
    TransactionResponse getTransactionsWithFilter(Long senderId, Long receiverId, TransactionType transactionType, int pageNum, int pageSize);
}
