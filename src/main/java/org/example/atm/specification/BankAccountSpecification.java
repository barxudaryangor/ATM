package org.example.atm.specification;

import org.example.atm.entities.BankAccount;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.data.jpa.domain.Specification;

public class BankAccountSpecification {
    public static Specification<BankAccount> hasAccountNum(String account_num) {
        return (root,query,cb) ->
                cb.like(cb.lower(root.get("account_num")), "%" + account_num + "%");
    }

    public static Specification<BankAccount> hasCustomerId(Long customerId) {
        return(root,query,cb) ->
                cb.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<BankAccount> hasBankId(Long bankId) {
        return(root,query,cb) ->
                cb.equal(root.get("bank").get("id"), bankId);
    }
}
