package org.example.atm.events;

import org.example.atm.dtos.TransactionDTO;
import org.example.atm.services_interfaces.TransactionService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TransactionAsyncHandler {

    private final TransactionService transactionService;

    public TransactionAsyncHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Async
    @EventListener
    public void handleTransactionCreated(TransactionCreatedEvent event) {
        System.out.println("⚙️ async thread: " + Thread.currentThread().getName());
        transactionService.createTransaction(event.getTransactionDTO());
    }
}

