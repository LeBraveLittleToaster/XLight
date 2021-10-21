package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.exceptions.ControlGroupNotFoundException;
import de.pschiessle.xlight.xlightserver.exceptions.ControlGroupProcessingException;
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

  /**
   * Returns all {@link MtsControlGroup} available
   * @return Flux of {@link MtsControlGroup}, can also be Flux.empty()
   */
  public Flux<MtsControlGroup> getAllControlGroups() {
    return mtsControlGroupRepository.findAll();
  }

  /**
   * Return the {@link MtsControlGroup} corresponding to the fiven controlGroupId
   * @param controlGroupId unique controlGroupId:String
   * @return Mono<MtsControlGroup> or Mono.empty()
   */
  public Mono<MtsControlGroup> getControlGroupByControlGroupId(String controlGroupId) {
    return mtsControlGroupRepository.findByControlGroupId(controlGroupId);
  }

  /**
   * Creating MtsControlGroup validated by {@link MtsControlGroupValidator#validateInsertControlGroup(String, List)}
   * @param name non-unigue name
   * @param mtsLightIds List of {@link de.pschiessle.xlight.xlightserver.components.MtsLight} ids which are controlled by thie group
   * @return {@link MtsControlGroup} instance from the database or Mono.error() if it failed
   */
  public Mono<MtsControlGroup> createControlGroup(String name, List<String> mtsLightIds) {
    return MtsControlGroupValidator.validateInsertControlGroup(name, mtsLightIds)
        .flatMap(group -> {
          group.setControlGroupId(IdGenerator.generateUUID());
          return mtsControlGroupRepository.save(group);
        })
        .onErrorResume(Mono::error);
  }

  /**
   * Adding given lightId to {@link MtsControlGroup}, checks if lightId exist and is valid
   * @param controlGroupId id the light is added to
   * @param lightId id of the light that should be added
   * @return {@link MtsControlGroup} after update
   */
  public Mono<MtsControlGroup> addLightIdToControlGroup(String controlGroupId, String lightId) {
    return mtsLightRepository
        .findMtsLightByLightId(lightId)
        .flatMap(light -> mtsControlGroupRepository
            .findByControlGroupId(controlGroupId)
            .flatMap(controlGroup -> mtsControlGroupRepository
                .save(controlGroup.addLightId(light.getLightId()))
            )
            .switchIfEmpty(Mono.error(
                new ControlGroupNotFoundException("No ControlGroup with id=" + controlGroupId))))
        .onErrorResume(Mono::error);
  }

  /**
   * Removes given lightId from {@link MtsControlGroup}, checks if lightId exist and is valid
   * @param controlGroupId id the light is removed rom
   * @param lightId id of the light that should be removed
   * @return {@link MtsControlGroup} after update
   */
  public Mono<MtsControlGroup> removeLightIdFromControlGroup(String controlGroupId,
      String lightId) {
    return mtsControlGroupRepository
        .findByControlGroupId(controlGroupId)
        .flatMap(controlGroup ->
            mtsControlGroupRepository
                .save(controlGroup.removeLightId(lightId))
        )
        .switchIfEmpty(Mono.error(new ControlGroupProcessingException(
            "Failed to delete ControlGroupId=" + controlGroupId)))
        .onErrorResume(Mono::error);
  }

  /**
   * Applies a LightState to all lights in this group, filters out all lights that doesn´t support the mode
   * @param mtsControlGroupId group that should be used
   * @param modeId mode that is set
   * @param values values for the mode
   * @return
   */
  public Mono<List<MtsLightState>> setModeToGroupById(String mtsControlGroupId, long modeId,
      List<MtsValue> values) {
    return mtsControlGroupRepository
        .findByControlGroupId(mtsControlGroupId)
        .flatMap(mtsControlGroup ->
            Mono.just(mtsControlGroup
                .getLightIds()
                .stream()
                .map(lightId -> Mono.just(Tuples.of(lightId, modeId, values)))
                .collect(Collectors.toList())))
        .flatMap(updaters ->
            mtsLightStateService
                .updateMtsLightStates(Flux.mergeSequential(updaters))
                .collectList())
        .onErrorResume(Mono::error);

  }
}
