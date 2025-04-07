package org.example.atm.Bank;

import org.example.atm.Human.Human;
import org.example.atm.Human.HumanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankService implements BankServiceInterface {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final BankUpdater bankUpdater;
    private final HumanRepository humanRepository;

    public BankService(BankRepository bankRepository, BankMapper bankMapper, BankUpdater bankUpdater, HumanRepository humanRepository) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
        this.bankUpdater = bankUpdater;
        this.humanRepository = humanRepository;
    }

    public List<BankDTO> getAllBanks() {
        return bankRepository.findAll().stream()
                .map(bankMapper::bankToDTO)
                .collect(Collectors.toList());
    }

    public BankDTO getBankById(Long id) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        return bankMapper.bankToDTO(bank);
    }

    public BankDTO createBank(BankDTO bankDTO) {
        Bank bank = bankMapper.dtoToBank(bankDTO);

        bank = bankRepository.save(bank);
        List<Human> humans = bank.getHumans();


        for(Human human : humans) {
            human.setBank(bank);
            human.getBank().setId(bank.getId());
        }


        humanRepository.saveAll(humans);

        Bank savedBank = bankRepository.save(bank);

        BankDTO savedBankDTO = bankMapper.bankToDTO(savedBank);
        for(int i = 0 ; i < savedBankDTO.getHumans().size(); i++) {
            savedBankDTO.getHumans().get(i).setBankId(savedBank.getId());
        }

        return savedBankDTO;


    }

    public BankDTO updateBank(Long id, BankDTO bankDTO) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        bankUpdater.updateBank(bank, bankDTO);

        return bankMapper.bankToDTO(bank);
    }

    public void deleteBank(Long id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        bankRepository.delete(bank);
    }

}