package org.example.atm.services_interfaces;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.dtos.CustomerDTO;

import java.util.List;

public interface BankService {
    List<BankDTO> getAllBanks();
    BankDTO getBankById(Long id);
    List<CustomerDTO> getAllCustomersByBankId(Long bankId);
    BankDTO createBank(BankDTO bankDTO);
    BankDTO updateBank(Long id, BankDTO bankDTO);
    void deleteBank(Long id);
    List<BankAccountDTO> getBankAccountsByBankId(Long bankId);
}
