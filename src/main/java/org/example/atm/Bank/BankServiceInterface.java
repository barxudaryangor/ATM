package org.example.atm.Bank;

import java.util.List;

public interface BankServiceInterface {

    List<BankDTO> getAllBanks();
    BankDTO getBankById(Long id);
    BankDTO createBank(BankDTO bankDTO);
    BankDTO updateBank(Long id, BankDTO bankDTO);
    void deleteBank(Long id);
}
