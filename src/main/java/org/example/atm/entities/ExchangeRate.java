package org.example.atm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double usdToAmd;

    private LocalDateTime localDateTime;

    public ExchangeRate(Double usdToAmd, LocalDateTime localDateTime) {
        this.usdToAmd = usdToAmd;
        this.localDateTime = localDateTime;
    }
}
