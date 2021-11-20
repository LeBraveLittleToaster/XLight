package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.exceptions.LightStateUpdateFailedException;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsLightStateValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

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

  public Flux<MtsLightState> updateLightStateByManipulators(
      List<MtsManipulator> manipulatorList) {
    List<Mono<Tuple3<String, Long, List<MtsValue>>>> updaters = manipulatorList
        .stream()
        .map(m -> Mono.just(
            Tuples.of(m.getLightId(), m.getState().getModeId(), m.getState().getValues())))
        .collect(Collectors.toList());
    return updateMtsLightStates(Flux.mergeSequential(updaters));
  }

  /**
   * @param lightStateUpdaters List( Tuple3(String lightId, String mtsModeId, List(MtsValue)
   *                           values))
   * @return
   */
  public Flux<MtsLightState> updateMtsLightStates(
      Flux<Tuple3<String, Long, List<MtsValue>>> lightStateUpdaters) {
    return lightStateUpdaters
        .flatMap(updater ->
            updateMtsLightState(
                updater.getT1(),
                updater.getT2(),
                updater.getT3()))
        .switchIfEmpty(Flux.error(
            new LightStateUpdateFailedException("Failed to update one or more lightstates")))
        .onErrorResume(Flux::error);
  }

  public Mono<MtsLightState> updateMtsLightState(String lightId, long modeId,
      List<MtsValue> values) {
    return mtsModeRepository
        .findByModeId(modeId)
        .flatMap(mode ->
            MtsLightStateValidator.validateInsertLightState(mode, values)
        )
        .zipWith(mtsLightRepository.findMtsLightByLightId(lightId))
        .flatMap(stateLightTuple -> updateAndSaveLightState(stateLightTuple.getT1(),
            stateLightTuple.getT2(), modeId))
        .onErrorResume(Mono::error);
  }

  private Mono<MtsLightState> updateAndSaveLightState(MtsLightState mtsLightState,
      MtsLight mtsLight, long modeId) {
    if (mtsLight.getSupportedModes().contains(modeId)) {
      mtsLight.setState(mtsLightState);
      return mtsLightRepository
          .save(mtsLight)
          .flatMap(savedLight -> Mono.just(savedLight.getState()));
    }
    return Mono.error(new LightStateUpdateFailedException("Mode not supported"));
  }
}
