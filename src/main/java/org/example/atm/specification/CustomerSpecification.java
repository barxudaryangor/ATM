package org.example.atm.specification;

import org.example.atm.entities.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CustomerSpecification {
    public static Specification<Customer> hasFirstName(String firstName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Customer> hasLastName(String lastName) {
        return(root,query,cb) ->
                cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Customer> hasBirthDate(LocalDate birthDate) {
        return(root,query,cb) ->
                cb.equal(root.get("birthDate"), birthDate);
    }
}
