package org.example.atm.jpa_repositories;

import org.example.atm.entities.Transaction;
import org.example.atm.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction,Long>, JpaSpecificationExecutor<Transaction> {

}
