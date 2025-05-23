package de.pschiessle.xlight.xlightserver.controller;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.controller.requests.CreateLightRequest;
import de.pschiessle.xlight.xlightserver.controller.requests.SetIsOnRequest;
import de.pschiessle.xlight.xlightserver.controller.requests.SetLightModeRequest;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import de.pschiessle.xlight.xlightserver.services.MtsLightStateService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.validation.Valid;
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
import reactor.core.publisher.Flux;
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
  public Flux<MtsLight> getLights() {
    return mtsLightService.getLights();
  }

  @PostMapping("/lights/create")
  public Mono<ResponseEntity<MtsLight>> addLight(@Valid @RequestBody CreateLightRequest req) {
    return mtsLightService
        .createLight(req.name(), req.location(), req.mac(), req.supportedModes())
        .map(createdLight ->
            new ResponseEntity<>(createdLight, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        });
  }

  @PutMapping("/lights/isOn/set")
  public Mono<ResponseEntity<MtsLight>> setLightIsOn(@Valid @RequestBody SetIsOnRequest req) {
    return mtsLightService.setLightIsOn(req.lightId(), req.isOn())
        .flatMap(
            updatedLight -> Mono.just(new ResponseEntity<>(updatedLight, HttpStatus.OK)))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
  }

  @PutMapping("/lights/mode/set")
  public Mono<ResponseEntity<MtsLightState>> setModeToState(
      @Valid @RequestBody SetLightModeRequest req) {
    return mtsLightStateService.updateMtsLightState(req.lightId(), req.modeId(), req.values())
        .map(savedState ->
            new ResponseEntity<>(savedState, HttpStatus.OK))
        .switchIfEmpty(
            Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
  }

  @PostMapping(value = "/lights/{lightId}/picture/set")
  public ResponseEntity<String> uploadImage(@PathVariable long lightId,
      @RequestBody() String image) throws IOException {
    System.out.println("INPUT=" + image.substring(0, 10));
    mtsLightService.setLightPicture(lightId, image.getBytes(StandardCharsets.UTF_8));
    return new ResponseEntity<>("SUCESS", HttpStatus.OK);
  }
}
