package org.example.atm.BankAccount;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.Human.Human;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account_num;
    private double balance;

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "human_id")
    private Human human;
}
