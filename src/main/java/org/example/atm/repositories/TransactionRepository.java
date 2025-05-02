package org.example.atm.repositories;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;

    @Transactional
    public void updateTransaction(Transaction transaction, TransactionDTO transactionDTO){

        if(transactionDTO.getReceiver() != null) {
            BankAccount receiver = bankAccountJpaRepository.findById(transactionDTO.getReceiver().id())
                    .orElseThrow(() -> new RuntimeException("receiver.not.found"));
            transaction.setReceiver(receiver);
        }

        if(transactionDTO.getSender() != null) {
            BankAccount sender = bankAccountJpaRepository.findById(transactionDTO.getSender().id())
                    .orElseThrow(()-> new RuntimeException("sender.not.found"));
            transaction.setSender(sender);
        }
        transaction.setTransactionType(TransactionType.valueOf(transactionDTO.getTransactionType()));
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTimestamp(transactionDTO.getTimestamp());
    }

    @Transactional
    public void transfer(Long senderId, Long receiverId, Double amount, TransactionType transactionType) {

        BankAccount sender = null;
        BankAccount receiver = null;

        if(amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount.must.be.positive");
        }
        if(senderId != null) {
           sender = bankAccountJpaRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("sender.not.found"));
        }

        if(receiverId != null) {
            receiver = bankAccountJpaRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("receiver.not.found"));
        }

        switch (transactionType) {
            case DEPOSIT:
                if(receiver == null) {
                    throw new IllegalArgumentException("receiver.must.be.specified");
                }
                receiver.setBalance(receiver.getBalance() + amount);
                bankAccountJpaRepository.save(receiver);
                break;


            case WITHDRAWAL:
                if(sender == null) {
                    throw new IllegalArgumentException("Sender must be specified for deposit");
                }
                if(sender.getBalance() < amount) {
                    throw new RuntimeException("Insufficient funds for withdrawal");
                }
                sender.setBalance(sender.getBalance() - amount);
                bankAccountJpaRepository.save(sender);
                break;


            case TRANSFER:
                if(sender == null || receiver == null) {
                    throw new IllegalArgumentException("Both sender and receiver must be specified for transfer");
                }

                if(sender.getBalance() < amount) {
                    throw new RuntimeException("Insufficient funds for transfer");
                }

                sender.setBalance(sender.getBalance() - amount);
                receiver.setBalance(sender.getBalance() + amount);

                bankAccountJpaRepository.save(sender);
                bankAccountJpaRepository.save(receiver);
                break;


            default:
                throw new IllegalArgumentException("Unsupported transaction type");
        }

        Transaction transaction = new Transaction();

        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        transactionJpaRepository.save(transaction);

    }
}
