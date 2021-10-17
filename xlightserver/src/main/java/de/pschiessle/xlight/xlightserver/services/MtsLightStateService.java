package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsLightStateValidator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MtsLightStateService {

  final MtsLightRepository mtsLightRepository;
  final MtsModeRepository mtsModeRepository;

  public MtsLightStateService(
      MtsLightRepository mtsLightRepository,
      MtsModeRepository mtsModeRepository) {
    this.mtsLightRepository = mtsLightRepository;
    this.mtsModeRepository = mtsModeRepository;
  }

  public Mono<MtsLightState> updateMtsLightState(String lightId, String mtsModeId,
      List<MtsValue> values) {
    return mtsLightRepository
        .findMtsLightByLightId(lightId).switchIfEmpty(Mono.empty())
        .flatMap(light ->
            mtsModeRepository
                .findByMtsModeId(mtsModeId)
                .flatMap(mode -> {
                  try {
                    MtsLightState state = MtsLightStateValidator.validateInsertLightState(mode,
                        values);
                    light.setState(state);
                    return mtsLightRepository
                        .save(light)
                        .flatMap(modifiedLight -> Mono.just(modifiedLight.getState()))
                        .switchIfEmpty(Mono.empty());
                  } catch (IndexMissmatchException e) {
                    log.error("State not well defined", e);
                    return Mono.empty();
                  }
                })
                .switchIfEmpty(Mono.empty())
        );
  }
}
