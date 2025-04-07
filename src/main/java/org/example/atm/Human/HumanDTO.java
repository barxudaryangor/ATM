package org.example.atm.Human;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.Bank.BankDTO;
import org.example.atm.BankAccount.BankAccountDTO;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HumanDTO {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private LocalDate birth_date;
    private Long bankId;
    private List<BankAccountDTO> bankAccounts;
}
