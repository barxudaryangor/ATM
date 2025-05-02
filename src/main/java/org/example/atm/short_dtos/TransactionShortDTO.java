package org.example.atm.short_dtos;

public record TransactionShortDTO(
        Long id,
        String accountNumSender,
        String accountNumReceiver) {
}
