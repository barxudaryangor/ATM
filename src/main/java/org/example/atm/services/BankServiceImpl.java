package org.example.atm.services;

import org.example.atm.dtos.BankAccountDTO;
import org.example.atm.dtos.BankDTO;
import org.example.atm.dtos.CustomerDTO;
import org.example.atm.entities.Bank;
import org.example.atm.entities.BankAccount;
import org.example.atm.jpa_repositories.BankAccountJpaRepository;
import org.example.atm.jpa_repositories.BankJpaRepository;
import org.example.atm.jpa_repositories.TransactionJpaRepository;
import org.example.atm.mappers.BankAccountMapper;
import org.example.atm.mappers.BankMapper;
import org.example.atm.repositories.BankRepository;
import org.example.atm.responses.BankPaginationResponse;
import org.example.atm.services_interfaces.BankService;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {
    private final BankJpaRepository bankJpaRepository;
    private final BankMapper bankMapper;
    private final BankRepository bankRepository;
    private final BankAccountJpaRepository bankAccountJpaRepository;



    public BankServiceImpl(BankJpaRepository bankJpaRepository, BankMapper bankMapper, BankRepository bankRepository, BankAccountJpaRepository bankAccountJpaRepository) {
        this.bankJpaRepository = bankJpaRepository;
        this.bankMapper = bankMapper;
        this.bankRepository = bankRepository;
        this.bankAccountJpaRepository = bankAccountJpaRepository;
    }


    public BankDTO bankToDTO(Bank bank) {
        if (bank == null) {
            throw new RuntimeException("bank.not.found");
        }

        BankDTO bankDTO = bankMapper.bankToDTO(bank);

        if(bank.getBankAccounts() != null) {
            List<BankAccountShortDTO> bankAccountShortDTOs = bank.getBankAccounts().stream()
                    .map(bankaccount -> new BankAccountShortDTO(
                            bankaccount.getId(),
                            bankaccount.getAccountNum() ,
                            bankaccount.getBalance()
                    )).toList();
            bankDTO.setBankAccounts(bankAccountShortDTOs);
        }


        return bankDTO;
    }

    public Bank dtoToBank(BankDTO bankDTO) {
        if (bankDTO == null) {
            throw new RuntimeException("bank.not.found");
        }

        Bank bank = bankMapper.dtoToBank(bankDTO);

        if(bankDTO.getBankAccounts() != null) {
            List<BankAccount> accounts = bankDTO.getBankAccounts().stream()
                    .map(shortDTO -> {
                        BankAccount account = new BankAccount();
                        account.setAccountNum(shortDTO.account_num());
                        account.setBalance(shortDTO.balance());
                        account.setBank(bank);
                        return account;
                    })
                    .toList();
            bank.setBankAccounts(accounts);
        }
        return bank;
    }

    @Override
    public List<BankDTO> getAllBanks() {
        return bankJpaRepository.findAll().stream()
                .map(this::bankToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankDTO getBankById(Long id) {
        Bank bank = bankJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank.not.found"));
        return bankToDTO(bank);
    }

    @Override
    public BankPaginationResponse getBanksWithFilter(String name, String location, int pageNum, int pageSize) {
        return bankRepository.getBanksWithFilter(
                name, location, pageNum, pageSize);
    }

    @Override
    public List<CustomerDTO> getAllCustomersByBankId(Long bankId) {
        return bankRepository.getAllCustomersByBankId(bankId);
    }

    @Override
    public BankDTO createBank(BankDTO bankDTO) {
         Bank bank = dtoToBank(bankDTO);
         return bankToDTO(bankJpaRepository.save(bank));
    }

    @Override
    public BankDTO updateBank(Long id, BankDTO bankDTO) {
        Bank bank = bankJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank.not.found"));
        bankRepository.updateBank(bank,bankDTO);
        return bankToDTO(bankJpaRepository.save(bank));
    }

    @Override
    public void deleteBank(Long id) {
        Bank bank = bankJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank.not.found"));
        bankJpaRepository.delete(bank);
    }

    @Override
    public List<BankAccountDTO> getBankAccountsByBankId(Long bankId) {
        return bankRepository.getBankAccountsByBankId(bankId);
    }




}
