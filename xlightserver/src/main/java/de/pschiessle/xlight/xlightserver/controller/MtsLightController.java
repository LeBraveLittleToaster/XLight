package de.pschiessle.xlight.xlightserver.controller;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import de.pschiessle.xlight.xlightserver.services.MtsLightStateService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@CrossOrigin(originPatterns = "*")
@RestController
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
  public Mono<ResponseEntity<MtsLight>> addLight(@RequestBody MtsLight mtsLight) {
    return mtsLightService
        .createLight(mtsLight)
        .flatMap(createdLight ->
            Mono.just(new ResponseEntity<>(createdLight, HttpStatus.OK)))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        });
  }

  @PutMapping("/lights/{lightId}/set")
  public Mono<ResponseEntity<MtsLight>> setLightIsOn(@PathVariable String lightId,
      @RequestParam boolean isOn) {
    return mtsLightService.setLightIsOn(lightId, isOn)
        .flatMap(
            updatedLight -> Mono.just(new ResponseEntity<>(updatedLight, HttpStatus.OK)))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        });
  }

  @PutMapping("/lights/{lightId}/state/{modeId}/set")
  public ResponseEntity<MtsLightState> setModeToState(@PathVariable long modeId,
      @PathVariable long lightId, @RequestBody List<MtsValue> values) {
    try {
      MtsLightState curState = mtsLightStateService.updateMtsLightState(lightId, modeId, values);
      return new ResponseEntity<>(curState, HttpStatus.CREATED);
    } catch (Exception e) {
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
