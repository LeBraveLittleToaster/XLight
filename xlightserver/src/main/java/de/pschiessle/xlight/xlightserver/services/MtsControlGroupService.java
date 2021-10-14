package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xlightserver.validator.MtsControlGroupValidator;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MtsControlGroupService {

  final MtsControlGroupRepository mtsControlGroupRepository;

  public MtsControlGroupService(
      MtsControlGroupRepository mtsControlGroupRepository) {
    this.mtsControlGroupRepository = mtsControlGroupRepository;
  }

  public Mono<MtsControlGroup> getControlGroupByControlGroupId(String controlGroupId){
    return mtsControlGroupRepository.findByControlGroupId(controlGroupId);
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
