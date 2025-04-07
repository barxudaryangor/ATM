package org.example.atm.Human;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.Bank.Bank;
import org.example.atm.BankAccount.BankAccount;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;
    private String last_name;
    private String email;
    private LocalDate birth_date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany (mappedBy = "human")
    @JsonManagedReference
    private List<BankAccount> bankAccounts;


}
