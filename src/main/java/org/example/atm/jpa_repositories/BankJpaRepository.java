package org.example.atm.jpa_repositories;

import org.example.atm.entities.Bank;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {
    @Override
    <S extends Bank> Page<S> findAll(Example<S> example, Pageable pageable);
}
