package org.example.atm.jpa_repositories;

import org.example.atm.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BankAccountJpaRepository extends JpaRepository<BankAccount,Long>, JpaSpecificationExecutor<BankAccount> {
    List<BankAccount> findAllByBankId(Long bankId);
    List<BankAccount> findAllByCustomerId(Long customerId);
}
