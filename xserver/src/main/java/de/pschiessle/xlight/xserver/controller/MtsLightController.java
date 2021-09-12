package de.pschiessle.xlight.xserver.controller;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api")
public class MtsLightController {
    final MtsLightRepository lightRepository;


    public MtsLightController(MtsLightRepository lightRepository) {
        this.lightRepository = lightRepository;
    }

    @PostMapping("/light/add")
    public ResponseEntity<MtsLight> addLight(@RequestBody MtsLight mtsLight){
        try {
            MtsLight _light = lightRepository.save(mtsLight);
            return new ResponseEntity<>(_light, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
