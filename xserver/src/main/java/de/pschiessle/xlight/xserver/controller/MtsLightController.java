package de.pschiessle.xlight.xserver.controller;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xserver.services.MtsLightService;
import de.pschiessle.xlight.xserver.services.MtsLightStateService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/api")
public class MtsLightController {

  final MtsLightService mtsLightService;
  final MtsLightStateService mtsLightStateService;

  public MtsLightController(MtsLightService mtsLightService,
      MtsLightStateService mtsLightStateService) {
    this.mtsLightService = mtsLightService;
    this.mtsLightStateService = mtsLightStateService;
  }

  @GetMapping("/lights")
  public ResponseEntity<List<MtsLight>> getLights(){
    try {
      List<MtsLight> _lights = mtsLightService.getLights();
      return new ResponseEntity<>(_lights, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/lights/add")
  public ResponseEntity<MtsLight> addLight(@RequestBody MtsLight mtsLight) {
    try {
      MtsLight _light = mtsLightService.createLight(mtsLight);
      return new ResponseEntity<>(_light, HttpStatus.CREATED);
    } catch (Exception | NoSufficientDataException e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/lights/state/{modeId}/set")
  public ResponseEntity<MtsLightState> setModeToState(@PathVariable long modeId, @RequestBody List<MtsValue> values) {
    try {
      MtsLightState curState = mtsLightStateService.updateMtsLightState(modeId, values);
      return new ResponseEntity<>(curState, HttpStatus.CREATED);
    } catch (IndexMissmatchException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


  }
}
