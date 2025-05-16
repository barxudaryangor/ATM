package org.example.atm.specification;

import org.example.atm.entities.Bank;
import org.springframework.data.jpa.domain.Specification;

public class BankSpecification {
    public static Specification<Bank> hasName(String name) {
        return (root,query,cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Bank> hasLocation(String location) {
        return (root,query,cb) ->
                cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }
}
