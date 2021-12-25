package de.pschiessle.xlight.xlightserver.controller;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import de.pschiessle.xlight.xlightserver.components.MtsMood;
import de.pschiessle.xlight.xlightserver.controller.requests.CreateMoodRequest;
import de.pschiessle.xlight.xlightserver.controller.requests.SetMoodRequest;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import de.pschiessle.xlight.xlightserver.services.MtsMoodService;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@CrossOrigin(originPatterns = "*")
@RestController
public class MtsMoodController {

  final MtsMoodService mtsMoodService;
  final MtsLightService mtsLightService;

  public MtsMoodController(MtsMoodService mtsMoodService,
      MtsLightService mtsLightService) {
    this.mtsMoodService = mtsMoodService;
    this.mtsLightService = mtsLightService;
  }

  @PutMapping("/moods/create")
  public Mono<ServerResponse> createMood(@Valid @RequestBody CreateMoodRequest req) {
    return mtsLightService.getLightsByLightIds(req.lightIds())
        .collectList()
        .flatMap(mtsLights ->
            mtsMoodService.createMood(req.name(), mtsLights.stream()
                .map(mtsLight -> new MtsManipulator(
                    mtsLight.getLightId(),
                    mtsLight.getState()))
                .collect(Collectors.toList())))
        .flatMap(v -> ServerResponse.ok().build())
        .switchIfEmpty(ServerResponse.badRequest().build())
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
  }

  @PostMapping("/moods/set")
  public Mono<ServerResponse> setMood(@Valid @RequestBody SetMoodRequest req){
    return mtsMoodService.setMood(req.moodId())
        .flatMap(v -> ServerResponse.ok().build())
        .switchIfEmpty(ServerResponse.badRequest().build())
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
  }
}
