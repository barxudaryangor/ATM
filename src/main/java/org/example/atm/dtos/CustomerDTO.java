package org.example.atm.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.entities.BankAccount;
import org.example.atm.short_dtos.BankAccountShortDTO;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @NotNull
    private Long id;

    @NotBlank(message = "first.name.can.not.be.empty")
    private String firstName;

    @NotBlank(message = "last.name.can.not.be.empty")
    private String lastName;

    @NotNull(message = "birthdate.is.required")
    @Past(message = "birthdate.must.be.in.the.past")
    private LocalDate birthDate;

    @NotEmpty
    @Valid
    private List<BankAccountShortDTO> bankAccounts;
}
