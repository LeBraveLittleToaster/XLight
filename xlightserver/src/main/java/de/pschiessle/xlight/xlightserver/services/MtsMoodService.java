package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import de.pschiessle.xlight.xlightserver.components.MtsMood;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsMoodRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MtsMoodService {

  final MtsMoodRepository mtsMoodRepository;
  final MtsLightService mtsLightService;
  final MtsLightStateService mtsLightStateService;

  public MtsMoodService(
      MtsMoodRepository mtsMoodRepository,
      MtsLightService mtsLightService,
      MtsLightStateService mtsLightStateService) {
    this.mtsMoodRepository = mtsMoodRepository;
    this.mtsLightService = mtsLightService;
    this.mtsLightStateService = mtsLightStateService;
  }


  public Mono<MtsMood> createMood(String name, List<MtsManipulator> manipulators) {
    //TODO: validate manipulators
    MtsMood mtsMood = MtsMood.builder()
        .moodId(IdGenerator.generateUUID())
        .name(name)
        .manipulatorList(manipulators)
        .build();
    return mtsMoodRepository.insert(mtsMood);
  }

  public Mono<List<Long>> setMood(String mtsMoodId) {
    return mtsMoodRepository
        .findByMoodId(mtsMoodId)
        .flatMap(mtsMood ->
            mtsLightStateService.updateLightStateByManipulators(mtsMood.getManipulatorList())
                .collectList())
        .flatMap(mtsLightStates -> Mono.just(
            mtsLightStates.stream().map(MtsLightState::getModeId).collect(
                Collectors.toList())))
        .switchIfEmpty(Mono.error(new Throwable("Not able to set lightstate, cause unknown")))
        .onErrorResume(Mono::error);
  }
}
