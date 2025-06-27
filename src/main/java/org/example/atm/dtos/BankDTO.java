package org.example.atm.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.atm.short_dtos.BankAccountShortDTO;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, message = "name.can.not.be.empty")
    private String name;

    @NotBlank
    @Size(min = 3, message = "location.can.not.be.empty")
    private String location;

    @NotEmpty
    @Valid
    private List<BankAccountShortDTO> bankAccounts;
}
