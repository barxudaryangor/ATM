package org.example.atm.BankAccount;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.Human.HumanDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BankAccountDTO {
    private Long id;
    private String account_num;
    private double balance;
    private Long humanId;
}
