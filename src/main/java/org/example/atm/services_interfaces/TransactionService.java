package org.example.atm.services_interfaces;

import org.example.atm.responses.TransactionPaginationResponse;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.dtos.TransactionResponse;
import org.example.atm.enums.TransactionType;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    TransactionDTO getTransactionById(Long id);
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO);
    void deleteTransaction(Long id);
    void transfer(Long senderId, Long receiverId, Double amount, TransactionType transactionType);
    TransactionResponse getTransactionsWithPagination(int pageNum, int pageSize);
    void publish(TransactionDTO transactionDTO);
    TransactionPaginationResponse getTransactionsWithFilter(Long senderId, Long receiverId, TransactionType transactionType, int pageNum, int pageSize);
}
