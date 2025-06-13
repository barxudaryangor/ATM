package org.example.atm.events;

import org.example.atm.dtos.TransactionDTO;
import org.springframework.context.ApplicationEvent;

public class TransactionCreatedEvent {
    private final TransactionDTO transactionDTO;


    public TransactionCreatedEvent(TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
    }

    public TransactionDTO getTransactionDTO() {
        return transactionDTO;
    }
}
