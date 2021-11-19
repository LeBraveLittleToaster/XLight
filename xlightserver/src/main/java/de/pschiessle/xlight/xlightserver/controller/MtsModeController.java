package de.pschiessle.xlight.xlightserver.controller;

import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.services.MtsModeService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(originPatterns = "*")
@RestController
public class MtsModeController {

  final MtsModeService mtsModeService;

  public MtsModeController(MtsModeService mtsModeService) {
    this.mtsModeService = mtsModeService;
  }

  @GetMapping("/modes")
  public Flux<MtsMode> getModes() {
    return mtsModeService.getModes();
  }

  @PutMapping("/modes/create")
  public Mono<ResponseEntity<MtsMode>> createMode(@Valid @RequestBody MtsMode mtsMode) {
    return mtsModeService
        .createMode(mtsMode.getModeId(), mtsMode.getName(), mtsMode.getInputs())
        .flatMap(
            mode -> Mono.just(new ResponseEntity<>(mode, HttpStatus.OK)))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
        .doOnError(e -> {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
  }

  @DeleteMapping("/modes/{modeId}")
  public Mono<ServerResponse> deleteMode(@PathVariable("modeId") String modeId) {
    System.out.println("Deleting modeId=" + modeId);
    return mtsModeService.deleteByMtsModeId(modeId)
        .flatMap(v -> ServerResponse.ok().build())
        .switchIfEmpty(ServerResponse.badRequest().build());
  }
}
