package org.example.atm.jpa_repositories;


import org.example.atm.entities.BankAccount;
import org.example.atm.responses.BankAccountPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BankAccountJpaRepository extends JpaRepository<BankAccount,Long>, JpaSpecificationExecutor<BankAccount> {
    List<BankAccount> findAllByBankId(Long bankId);

    List<BankAccount> findAllByCustomerId(Long customerId);

    static Specification<BankAccount> hasAccountNum(String accountNum) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("accountNum")), "%" + accountNum.toLowerCase() + "%");
    }

    static Specification<BankAccount> hasCustomerId(Long customerId) {
        return (root, query, cb) ->
                cb.equal(root.get("customer").get("id"), customerId);
    }

    static Specification<BankAccount> hasBankId(Long bankId) {
        return (root, query, cb) ->
                cb.equal(root.get("bank").get("id"), bankId);
    }

    default Page<BankAccount> getBankAccountsWithFilter(String accountNum, Long customerId, Long bankId, int pageNum, int pageSize) {
        Specification<BankAccount> spec = Specification.where(null);

        spec = Optional.ofNullable(accountNum)
                .filter(s -> !s.isBlank())
                .map(BankAccountJpaRepository::hasAccountNum)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(customerId)
                .map(BankAccountJpaRepository::hasCustomerId)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(bankId)
                .map(BankAccountJpaRepository::hasBankId)
                .map(spec::and)
                .orElse(spec);

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return findAll(spec, pageable);
    }
}

