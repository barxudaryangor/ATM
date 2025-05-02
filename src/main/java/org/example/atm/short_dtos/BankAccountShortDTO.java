package org.example.atm.short_dtos;

public record BankAccountShortDTO(
        Long id,
        String account_num,
        Double balance
) {}
