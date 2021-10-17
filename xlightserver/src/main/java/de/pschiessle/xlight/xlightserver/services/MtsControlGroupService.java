package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsControlGroupValidator;
import de.pschiessle.xlight.xlightserver.validator.MtsLightStateValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MtsControlGroupService {

  final MtsControlGroupRepository mtsControlGroupRepository;
  final MtsLightRepository mtsLightRepository;
  final MtsModeRepository mtsModeRepository;

  public MtsControlGroupService(
      MtsControlGroupRepository mtsControlGroupRepository,
      MtsLightRepository mtsLightRepository,
      MtsModeRepository mtsModeRepository) {
    this.mtsControlGroupRepository = mtsControlGroupRepository;
    this.mtsLightRepository = mtsLightRepository;
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
    try {
      MtsControlGroup mtsControlGroup = MtsControlGroupValidator.validateInsertControlGroup(name,
          mtsLightIds);
      mtsControlGroup.setControlGroupId(IdGenerator.generateUUID());
      return mtsControlGroupRepository.save(mtsControlGroup);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      return Mono.error(e);
    }
  }

  public Mono<List<String>> setModeToGroupById(String mtsControlGroupId, String mtsModeId,
      List<MtsValue> values) {
    return Mono.zip(
        mtsControlGroupRepository.findByControlGroupId(mtsControlGroupId),
        mtsModeRepository.findByMtsModeId(mtsModeId)
    ).flatMap(
        groupModeTuple -> {
          try {
            MtsLightState state = MtsLightStateValidator.validateInsertLightState(
                groupModeTuple.getT2(), values);
            return mtsLightRepository
                .findAllByLightIdIn(groupModeTuple.getT1().getLightIds())
                .collectList()
                .flatMap(lightsToUpdate ->
                    mtsLightRepository.saveAll(
                        lightsToUpdate
                            .stream()
                            .peek(light -> light.setState(state))
                            .collect(Collectors.toList())).map(MtsLight::getLightId).collectList())
                .switchIfEmpty(Mono.empty())
                .switchIfEmpty(Mono.empty());
          } catch (IndexMissmatchException e) {
            log.error("Failed to validate state", e);
            return Mono.empty();
          }
        }
    );
    /*
    return mtsControlGroupRepository
        .findByControlGroupId(mtsControlGroupId)
        .flatMap(group ->

        );

     */
  }
}
