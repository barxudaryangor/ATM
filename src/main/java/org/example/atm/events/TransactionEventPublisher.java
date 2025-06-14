package org.example.atm.events;

import org.example.atm.dtos.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public TransactionEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(TransactionDTO transactionDTO) {
        applicationEventPublisher.publishEvent(new TransactionCreatedEvent(transactionDTO));
    }
}
