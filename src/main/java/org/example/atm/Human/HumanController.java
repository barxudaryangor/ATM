package org.example.atm.Human;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm/humans")
public class HumanController {
    private final HumanServiceInterface humanServiceInterface;

    public HumanController(HumanServiceInterface humanServiceInterface) {
        this.humanServiceInterface = humanServiceInterface;
    }

    @GetMapping
    public ResponseEntity<List<HumanDTO>> getAllHumans() {
        return ResponseEntity.ok(humanServiceInterface.getAllHumans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HumanDTO> getHumanById(@PathVariable Long id) {
        return ResponseEntity.ok(humanServiceInterface.getHumanById(id));
    }

    @PostMapping
    public ResponseEntity<HumanDTO> createHuman(@RequestBody HumanDTO humanDTO) {
        return ResponseEntity.status(201).body(humanServiceInterface.createHuman(humanDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HumanDTO> updateHuman(@PathVariable Long id, @RequestBody HumanDTO humanDTO) {
        return ResponseEntity.ok(humanServiceInterface.updateHuman(id,humanDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHuman(@PathVariable Long id) {
        humanServiceInterface.deleteHuman(id);
        return ResponseEntity.noContent().build();
    }
}
