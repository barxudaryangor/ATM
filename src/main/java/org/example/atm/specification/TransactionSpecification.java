package org.example.atm.specification;

import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {
    public static Specification<Transaction> hasSenderId(Long senderId) {
        return (root, query, cb) ->
            cb.equal(root.get("sender").get("id"), senderId);
    }

    public static Specification<Transaction> hasReceiverId(Long receiverId) {
        return(root, query, cb) ->
                cb.equal(root.get("receiver").get("id"), receiverId);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return(root, query, cb) ->
                cb.equal(root.get("transactionType"), type);
    }

}
