package org.example.atm.jpa_repositories;

import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    List<Customer> findAllByBankAccounts_Bank_Id(Long bankId);

    static Specification<Customer> hasFirstName(String firstName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    static Specification<Customer> hasLastName(String lastName) {
        return (root,query,cb) ->
                cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    static Specification<Customer> hasBirthDate(LocalDate birthDate) {
        return (root,query,cb) ->
                cb.equal(root.get("birthDate"), birthDate);
    }

    default Page<Customer> getCustomersWithFilter(String firstName, String lastName, LocalDate birthDate, int pageNum, int pageSize) {
        Specification<Customer> spec = Specification.where(null);

        spec = Optional.ofNullable(firstName)
                .filter(s -> !s.isBlank())
                .map(CustomerJpaRepository::hasFirstName)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(lastName)
                .filter(s -> !s.isBlank())
                .map(CustomerJpaRepository::hasLastName)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(birthDate)
                .map(CustomerJpaRepository::hasBirthDate)
                .map(spec::and)
                .orElse(spec);

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return findAll(spec, pageable);
    }
}
