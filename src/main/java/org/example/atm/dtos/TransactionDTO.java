package org.example.atm.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.short_dtos.BankAccountShortDTO;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;

    @NotNull(message = "amount.can.not.be.empty")
    @DecimalMin(value = "0.0", message = "balance.can.not.be.empty")
    @Digits(integer = 12, fraction = 2, message = "balance.must.be.a.valid.monetary.money")
    private Double amount;

    @NotNull(message = "timestamp.is.required")
    @PastOrPresent(message = "timestamp.must.be.in.the.past.or.present")
    private LocalDateTime timestamp;

    @NotBlank(message = "transaction.type.is.required")
    private String transactionType;

    @NotNull(message = "sender.can.not.be.null")
    @Valid
    private BankAccountShortDTO sender;

    @NotNull(message = "receiver.can.not.be.null")
    @Valid
    private BankAccountShortDTO receiver;
}
