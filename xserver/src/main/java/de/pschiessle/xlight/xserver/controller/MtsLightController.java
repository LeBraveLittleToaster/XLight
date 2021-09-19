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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(originPatterns = "*")
@Controller
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

  @GetMapping(value = "/lights", produces = "application/json; charset=utf-8")
  public ResponseEntity<List<MtsLight>> getLights() {
    try {
      List<MtsLight> _lights = mtsLightService.getLights();
      return new ResponseEntity<>(_lights, HttpStatus.OK);
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

  @PutMapping("/lights/{lightId}/set")
  public ResponseEntity<Void> setLightIsOn(@PathVariable long lightId, @RequestParam boolean isOn) {
    mtsLightService.setLightIsOn(lightId, isOn);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/lights/{lightId}/state/{modeId}/set")
  public ResponseEntity<MtsLightState> setModeToState(@PathVariable long modeId,
      @PathVariable long lightId, @RequestBody List<MtsValue> values) {
    try {
      MtsLightState curState = mtsLightStateService.updateMtsLightState(lightId, modeId, values);
      return new ResponseEntity<>(curState, HttpStatus.CREATED);
    } catch (IndexMissmatchException | NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/lights/{lightId}/picture/set")
  public ResponseEntity<String> uploadImage(@PathVariable long lightId,
      @RequestBody() String image) throws IOException {
    System.out.println("INPUT=" + image.substring(0, 10));
    mtsLightService.setLightPicture(lightId, image.getBytes(StandardCharsets.UTF_8));
    return new ResponseEntity<>("SUCESS", HttpStatus.OK);
  }
}
