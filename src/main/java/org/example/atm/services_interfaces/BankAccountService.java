package org.example.atm.services_interfaces;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.entities.BankAccount;
import org.example.atm.responses.BankAccountPaginationResponse;

import java.util.List;

public interface BankAccountService {
    List<BankAccountDTO> getAllBankAccounts();
    BankAccountDTO getBankAccountById(Long id);
    BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO);
    BankAccountDTO updateBankAccount(Long id, BankAccountDTO bankAccountDTO);
    void deleteBankAccount(Long id);
    BankAccountPaginationResponse getBankAccountsWithFilter(String account_num, Long customerId, Long bankId, int pageNum, int pageSize);
}
