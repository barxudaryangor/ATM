package org.example.atm.services;

import jakarta.annotation.PostConstruct;
import org.example.atm.responses.TransactionPaginationResponse;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final BankAccountJpaRepository bankAccountJpaRepository;

    private final BlockingQueue<TransactionDTO> transactionQueue = new LinkedBlockingQueue<>();


    public TransactionServiceImpl(TransactionJpaRepository transactionJpaRepository, TransactionMapper transactionMapper, TransactionRepository transactionRepository, BankAccountJpaRepository bankAccountJpaRepository, BankAccountMapper bankAccountMapper) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.bankAccountJpaRepository = bankAccountJpaRepository;
    }




    public TransactionDTO transactionToDTO(Transaction transaction) {
        if (transaction == null) {
            throw new RuntimeException("transaction.not.found");
        }

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

    public Transaction dtoToTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            throw new RuntimeException("transaction.not.found");
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
    public TransactionPaginationResponse getTransactionsWithFilter(Long senderId, Long receiverId, TransactionType transactionType, int pageNum, int pageSize) {
        return transactionRepository.getTransactionsWithFilter(
                senderId, receiverId, transactionType, pageNum, pageSize);

    }


    public void publish(TransactionDTO transactionDTO) {
        try {
            System.out.println("Adding to Queue " + transactionDTO);
            transactionQueue.put(transactionDTO);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to publish transaction to queue", e);
        }
    }


    public void processQueue() {
        while (true) {
            try {
                TransactionDTO dto = transactionQueue.take();
                System.out.println("Getting from Queue" + dto);
                createTransaction(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PostConstruct
    public void initQueue() {
        Thread thread = new Thread(this::processQueue);
        thread.setDaemon(true);
        thread.start();
    }




}
