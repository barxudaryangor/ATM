package org.example.atm.jpa_repositories;

import org.example.atm.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction,Long>, JpaSpecificationExecutor<Transaction> {
}
