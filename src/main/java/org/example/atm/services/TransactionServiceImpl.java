package org.example.atm.services;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.TransactionDTO;
import org.example.atm.dtos.TransactionResponse;
import org.example.atm.entities.BankAccount;
import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.example.atm.mappers.BankAccountMapper;
import org.example.atm.mappers.TransactionMapper;
import org.example.atm.repositories.TransactionRepository;
import org.example.atm.services_interfaces.TransactionService;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.example.atm.specification.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountMapper bankAccountMapper;

    public TransactionServiceImpl(TransactionJpaRepository transactionJpaRepository, TransactionMapper transactionMapper, TransactionRepository transactionRepository, BankAccountJpaRepository bankAccountJpaRepository, BankAccountMapper bankAccountMapper) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.bankAccountJpaRepository = bankAccountJpaRepository;
        this.bankAccountMapper = bankAccountMapper;
    }


    public TransactionDTO transactionToDTO(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDTO transactionDTO = transactionMapper.transactionToDTO(transaction);

        if(transaction.getReceiver() != null) {
            BankAccountShortDTO receiverShortDTO = new BankAccountShortDTO(
                    transaction.getReceiver().getId(),
                    transaction.getReceiver().getAccount_num(),
                    transaction.getReceiver().getBalance()
            );
            transactionDTO.setReceiver(receiverShortDTO);
        }

        if (transaction.getSender() != null) {
            BankAccountShortDTO senderShortDTO = new BankAccountShortDTO(
                    transaction.getSender().getId(),
                    transaction.getSender().getAccount_num(),
                    transaction.getSender().getBalance()
            );
            transactionDTO.setSender(senderShortDTO);
        }
        return transactionDTO;
    }

    public Transaction dtoToTransaction(TransactionDTO transactionDTO) {
        if ( transactionDTO == null ) {
            return null;
        }

        Transaction transaction = transactionMapper.dtoToTransaction(transactionDTO);

        if(transactionDTO.getReceiver() != null) {
            BankAccount receiver = bankAccountJpaRepository.findById(transactionDTO.getReceiver().id())
                    .orElseThrow(() -> new RuntimeException("receiver.not.found"));
            transaction.setReceiver(receiver);
        }

        if(transactionDTO.getSender() != null) {
            BankAccount sender = bankAccountJpaRepository.findById(transactionDTO.getSender().id())
                    .orElseThrow(() -> new RuntimeException("sender.not.found"));
            transaction.setSender(sender);
        }


        return transaction;
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionJpaRepository.findAll().stream()
                .map(this::transactionToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("transaction.not.found"));
        return transactionToDTO(transactionJpaRepository.save(transaction));
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = dtoToTransaction(transactionDTO);
        return transactionToDTO(transactionJpaRepository.save(transaction));
    }

    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("transaction.not.found"));
        transactionRepository.updateTransaction(transaction,transactionDTO);
        return transactionToDTO(transactionJpaRepository.save(transaction));
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("transaction.not.found"));
        transactionJpaRepository.delete(transaction);
    }

    @Override
    public void transfer(Long senderId, Long receiverId, Double amount, TransactionType transactionType) {
        transactionRepository.transfer(senderId, receiverId, amount, transactionType);
    }

    @Override
    public TransactionResponse getTransactionsWithPagination(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Transaction> transactions = transactionJpaRepository.findAll(pageable);
        List<Transaction> transactionsList = transactions.getContent();
        List<TransactionDTO> content = transactionsList.stream().map(this::transactionToDTO).toList();

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setContent(content);
        transactionResponse.setPageNum(transactions.getNumber());
        transactionResponse.setPageSize(transactions.getSize());
        transactionResponse.setTotalElements(transactions.getTotalElements());
        transactionResponse.setTotalPages(transactions.getTotalPages());
        transactionResponse.setLast(transactions.isLast());

        return transactionResponse;
    }

    @Override
    public TransactionResponse getTransactionsWithFilter(Long senderId, Long receiverId, TransactionType transactionType, int pageNum, int pageSize) {
        Specification<Transaction> spec = Specification.where(null);

        if(senderId != null) {
            spec = spec.and(TransactionSpecification.hasSenderId(senderId));
        }

        if(receiverId != null) {
            spec = spec.and(TransactionSpecification.hasReceiverId(receiverId));
        }

        if(transactionType != null) {
            spec = spec.and(TransactionSpecification.hasType(transactionType));
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Transaction> page = transactionJpaRepository.findAll(spec, pageable);
        List<TransactionDTO> content = page.getContent().stream().map(this::transactionToDTO).toList();

        return new TransactionResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }


}
