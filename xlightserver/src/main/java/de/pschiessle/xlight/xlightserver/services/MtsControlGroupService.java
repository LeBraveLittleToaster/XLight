package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsControlGroupValidator;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MtsControlGroupService {

  final MtsControlGroupRepository mtsControlGroupRepository;
  final MtsLightRepository mtsLightRepository;

  public MtsControlGroupService(
      MtsControlGroupRepository mtsControlGroupRepository,
      MtsLightRepository mtsLightRepository) {
    this.mtsControlGroupRepository = mtsControlGroupRepository;
    this.mtsLightRepository = mtsLightRepository;
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

  public void setModeToGroupById(long groupId, Long modeId, List<MtsValue> values) {

  }
}
