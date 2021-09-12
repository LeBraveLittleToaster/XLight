package de.pschiessle.xlight.xserver.controller;

import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api")
public class MtsModeController {
    final MtsModeRepository mtsModeRepository;

    public MtsModeController(MtsModeRepository mtsModeRepository) {
        this.mtsModeRepository = mtsModeRepository;
    }

    @PostMapping("/modes/create")
    public ResponseEntity<MtsMode> createMode(@RequestBody MtsMode mtsMode) {
        try {
            MtsMode _mode = mtsModeRepository.save(new MtsMode(mtsMode.getModeId(), Instant.now().toEpochMilli(), mtsMode.getInputs()));
            return new ResponseEntity<>(_mode, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/modes/{modeId}")
    public ResponseEntity<Void> deleteMode(@PathVariable("modeId") long modeId) {
        try {
            mtsModeRepository.deleteByModeIdEquals(modeId);
            System.out.println("Deleting modeId=" + modeId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
