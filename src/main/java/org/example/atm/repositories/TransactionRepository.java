package org.example.atm.repositories;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.example.atm.mappers.TransactionMapper;
import org.example.atm.responses.TransactionPaginationResponse;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDTO transactionToDTO(Transaction transaction) {

        TransactionDTO transactionDTO = transactionMapper.transactionToDTO(transaction);

        if(transaction.getReceiver() != null) {
            BankAccountShortDTO receiverShortDTO = new BankAccountShortDTO(
                    transaction.getReceiver().getId(),
                    transaction.getReceiver().getAccountNum() ,
                    transaction.getReceiver().getBalance()
            );
            transactionDTO.setReceiver(receiverShortDTO);
        }

        if (transaction.getSender() != null) {
            BankAccountShortDTO senderShortDTO = new BankAccountShortDTO(
                    transaction.getSender().getId(),
                    transaction.getSender().getAccountNum() ,
                    transaction.getSender().getBalance()
            );
            transactionDTO.setSender(senderShortDTO);
        }
        return transactionDTO;
    }

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
                    throw new IllegalArgumentException("sender.must.be.specified.for.deposit");
                }
                if(sender.getBalance() < amount) {
                    throw new RuntimeException("insufficient.funds.for.withdrawal");
                }
                sender.setBalance(sender.getBalance() - amount);
                bankAccountJpaRepository.save(sender);
                break;


            case TRANSFER:
                if(sender == null || receiver == null) {
                    throw new IllegalArgumentException("sender.and.receiver.must.be.specified.for.transfer");
                }

                if(sender.getBalance() < amount) {
                    throw new RuntimeException("insufficient.funds.for.transfer");
                }

                sender.setBalance(sender.getBalance() - amount);
                receiver.setBalance(receiver.getBalance() + amount);

                bankAccountJpaRepository.save(sender);
                bankAccountJpaRepository.save(receiver);
                break;


            default:
                throw new IllegalArgumentException("unsupported.transaction.type");
        }

        Transaction transaction = new Transaction();

        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        transactionJpaRepository.save(transaction);

    }

    public TransactionPaginationResponse getTransactionsWithFilter(Long senderId, Long receiverId, TransactionType transactionType, int pageNum, int pageSize) {
        Page<Transaction> page = transactionJpaRepository.getTransactionsWithFilter(
                senderId, receiverId, transactionType, pageNum, pageSize);

        Page<TransactionDTO> dtoPage = page.map(this::transactionToDTO);
        return new TransactionPaginationResponse(dtoPage);

    }
}
