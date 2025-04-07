package org.example.atm.Human;

import java.util.List;

public interface HumanServiceInterface {
    List<HumanDTO> getAllHumans();
    HumanDTO getHumanById(Long id);
    HumanDTO createHuman(HumanDTO humanDTO);
    HumanDTO updateHuman(Long id, HumanDTO humanDTO);
    void deleteHuman(Long id);
}
