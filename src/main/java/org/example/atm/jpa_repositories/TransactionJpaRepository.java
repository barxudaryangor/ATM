package org.example.atm.jpa_repositories;


import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TransactionJpaRepository extends JpaRepository<Transaction,Long>, JpaSpecificationExecutor<Transaction> {
    static Specification<Transaction> hasSenderId(Long senderId) {
        return (root, query, cb) ->
                cb.equal(root.get("sender").get("id"), senderId);
    }

    static Specification<Transaction> hasReceiverId(Long receiverId) {
        return(root, query, cb) ->
                cb.equal(root.get("receiver").get("id"), receiverId);
    }

    static Specification<Transaction> hasType(TransactionType type) {
        return(root, query, cb) ->
                cb.equal(root.get("transactionType"), type);
    }

    default Page<Transaction> getTransactionsWithFilter(Long senderId, Long receiverId, TransactionType transactionType, int pageNum, int pageSize) {
        Specification<Transaction> spec = Specification.where(null);

        spec = Optional.ofNullable(senderId)
                .map(TransactionJpaRepository::hasSenderId)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(receiverId)
                .map(TransactionJpaRepository::hasReceiverId)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(transactionType)
                .map(TransactionJpaRepository::hasType)
                .map(spec::and)
                .orElse(spec);

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return findAll(spec, pageable);

    }
}
