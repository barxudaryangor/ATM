package org.example.atm.jpa_repositories;

import org.example.atm.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {

}
