package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.exceptions.LightStateUpdateFailedException;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsControlGroupValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Slf4j
@Service
public class MtsControlGroupService {

  final MtsControlGroupRepository mtsControlGroupRepository;
  final MtsLightRepository mtsLightRepository;
  final MtsLightStateService mtsLightStateService;
  final MtsModeRepository mtsModeRepository;

  public MtsControlGroupService(
      MtsControlGroupRepository mtsControlGroupRepository,
      MtsLightRepository mtsLightRepository,
      MtsLightStateService mtsLightStateService,
      MtsModeRepository mtsModeRepository) {
    this.mtsControlGroupRepository = mtsControlGroupRepository;
    this.mtsLightRepository = mtsLightRepository;
    this.mtsLightStateService = mtsLightStateService;
    this.mtsModeRepository = mtsModeRepository;
  }

  public Mono<MtsControlGroup> getControlGroupByControlGroupId(String controlGroupId) {
    return mtsControlGroupRepository.findByControlGroupId(controlGroupId);
  }

  public Mono<MtsControlGroup> addLightIdToControlGroup(String controlGroupId, String lightId) {
    return mtsLightRepository
        .findMtsLightByLightId(lightId)
        .flatMap(light ->
            mtsControlGroupRepository
                .findByControlGroupId(controlGroupId)
                .flatMap(controlGroup ->
                    mtsControlGroupRepository
                        .save(controlGroup.addLightId(light.getLightId()))
                        .switchIfEmpty(Mono.empty())
                )
                .switchIfEmpty(Mono.empty()))
        .switchIfEmpty(Mono.empty());
  }

  public Mono<MtsControlGroup> removeLightIdFromControlGroup(String controlGroupId,
      String lightId) {
    return mtsControlGroupRepository
        .findByControlGroupId(controlGroupId)
        .flatMap(controlGroup ->
            mtsControlGroupRepository
                .save(controlGroup.removeLightId(lightId))
                .switchIfEmpty(Mono.empty())
        )
        .switchIfEmpty(Mono.empty());
  }

  public Flux<MtsControlGroup> getAllControlGroups() {
    return mtsControlGroupRepository.findAll();
  }

  public Mono<MtsControlGroup> createControlGroup(String name, List<String> mtsLightIds) {

    return MtsControlGroupValidator.validateInsertControlGroup(name, mtsLightIds)
        .flatMap(group -> {
          group.setControlGroupId(IdGenerator.generateUUID());
          return mtsControlGroupRepository.save(group);
        })
        .onErrorResume(Mono::error);
  }

  public Mono<List<MtsLightState>> setModeToGroupById(String mtsControlGroupId, String mtsModeId,
      List<MtsValue> values) {
    return mtsControlGroupRepository
        .findByControlGroupId(mtsControlGroupId)
        .flatMap(mtsControlGroup ->
            Mono.just(mtsControlGroup
                .getLightIds()
                .stream()
                .map(lightId -> Mono.just(Tuples.of(lightId, mtsModeId, values)))
                .collect(Collectors.toList())))
        .flatMap(updaters ->
            mtsLightStateService
                .updateMtsLightStates(Flux.mergeSequential(updaters))
                .collectList())
        .switchIfEmpty(Mono.error(new LightStateUpdateFailedException(
            "Failed to found one or more light or the mode")))
        .onErrorResume(Mono::error);
    
  }
}
