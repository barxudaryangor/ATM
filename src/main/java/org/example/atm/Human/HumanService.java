package org.example.atm.Human;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HumanService implements HumanServiceInterface{
    private final HumanRepository humanRepository;
    private final HumanMapper humanMapper;
    private final HumanUpdater humanUpdater;

    public HumanService(HumanRepository humanRepository, HumanMapper humanMapper, HumanUpdater humanUpdater) {
        this.humanRepository = humanRepository;
        this.humanMapper = humanMapper;
        this.humanUpdater = humanUpdater;
    }

    public List<HumanDTO> getAllHumans(){
        return humanRepository.findAll().stream()
                .map(humanMapper::humanToDTO)
                .collect(Collectors.toList());
    }

    public HumanDTO getHumanById(Long id) {
        Human human = humanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Human not found"));

        return humanMapper.humanToDTO(human);
    }

    public HumanDTO createHuman(HumanDTO humanDTO) {
        Human human = humanMapper.dtoToHuman(humanDTO);
        return humanMapper.humanToDTO(humanRepository.save(human));
    }

    public HumanDTO updateHuman(Long id, HumanDTO humanDTO) {
        Human human = humanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Human not found"));

        humanUpdater.updateHuman(human, humanDTO);
        return humanMapper.humanToDTO(humanRepository.save(human));
    }

    public void deleteHuman(Long id) {
        Human human = humanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Human not found"));
        humanRepository.delete(human);
    }
}
