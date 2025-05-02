package org.example.atm.jpa_repositories;

import org.example.atm.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerJpaRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByBankAccounts_Bank_Id(Long bankId);
}
