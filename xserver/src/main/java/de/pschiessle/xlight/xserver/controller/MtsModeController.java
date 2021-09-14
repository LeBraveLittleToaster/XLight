package de.pschiessle.xlight.xserver.controller;

import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xserver.services.MtsModeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api")
public class MtsModeController {
    final MtsModeRepository mtsModeRepository;
    final MtsModeService mtsModeService;

    public MtsModeController(MtsModeRepository mtsModeRepository,
        MtsModeService mtsModeService) {
        this.mtsModeRepository = mtsModeRepository;
        this.mtsModeService = mtsModeService;
    }

    @PostMapping("/modes/create")
    public ResponseEntity<MtsMode> createMode(@RequestBody MtsMode mtsMode) {
        try {
            MtsMode _mode = mtsModeService.createMode(mtsMode.getModeId(), mtsMode.getInputs());
            return new ResponseEntity<>(_mode, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
