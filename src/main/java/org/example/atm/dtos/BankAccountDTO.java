package org.example.atm.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.entities.Bank;
import org.example.atm.entities.Customer;
import org.example.atm.entities.Transaction;
import org.example.atm.short_dtos.BankShortDTO;
import org.example.atm.short_dtos.CustomerShortDTO;
import org.example.atm.short_dtos.TransactionShortDTO;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {

    @NotNull
    private Long id;

    @Size(min = 3, message = "account_num.can.not.be.empty")
    private String account_num;

    @NotNull(message = "balance.can.not.be.empty")
    @DecimalMin(value = "0.0", message = "balance.can.not.be.empty")
    @Digits(integer = 12, fraction = 2, message = "balance.must.be.a.valid.monetary.money")
    private Double balance;

    @NotNull
    @Valid
    private CustomerShortDTO customer;

    @NotNull
    @Valid
    private BankShortDTO bank;

    @NotEmpty
    @Valid
    private List<TransactionShortDTO> sentTransactions;

    @NotEmpty
    @Valid
    private List<TransactionShortDTO> receivedTransactions;
}
