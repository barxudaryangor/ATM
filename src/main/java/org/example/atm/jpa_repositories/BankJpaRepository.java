package org.example.atm.jpa_repositories;


import org.example.atm.entities.Bank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BankJpaRepository extends JpaRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {

    static Specification<Bank> hasName(String name) {
        return (root,query,cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    static Specification<Bank> hasLocation(String location) {
        return (root,query,cb) ->
                cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    default Page<Bank> getBanksWithFilter(String name, String location, int pageNum, int pageSize) {
        Specification<Bank> spec = Specification.where(null);

        spec = Optional.ofNullable(name)
                .filter(s -> !s.isBlank())
                .map(BankJpaRepository::hasName)
                .map(spec::and)
                .orElse(spec);

        spec = Optional.ofNullable(location)
                .filter(s -> !s.isBlank())
                .map(BankJpaRepository::hasLocation)
                .map(spec::and)
                .orElse(spec);

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return findAll(spec, pageable);
    }
}
