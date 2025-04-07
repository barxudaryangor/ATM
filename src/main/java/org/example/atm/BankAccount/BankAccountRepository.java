package org.example.atm.BankAccount;

import org.example.atm.Human.Human;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByHuman(Human human);
}
