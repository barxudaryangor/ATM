package org.example.atm.jpa_repositories;

import org.example.atm.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction,Long> {
}
