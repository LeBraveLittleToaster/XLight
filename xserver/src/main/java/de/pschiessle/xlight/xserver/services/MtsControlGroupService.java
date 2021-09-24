package de.pschiessle.xlight.xserver.services;

import de.pschiessle.xlight.xserver.components.MtsControlGroup;
import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MtsControlGroupService {

  final MtsControlGroupRepository mtsControlGroupRepository;
  final MtsLightRepository mtsLightRepository;
  final MtsLightStateService mtsLightStateService;

  public MtsControlGroupService(
      MtsControlGroupRepository mtsControlGroupRepository,
      MtsLightRepository mtsLightRepository,
      MtsLightStateService mtsLightStateService) {
    this.mtsControlGroupRepository = mtsControlGroupRepository;
    this.mtsLightRepository = mtsLightRepository;

    this.mtsLightStateService = mtsLightStateService;
  }

  @Transactional
  public MtsControlGroup createControlGroup(String name, List<Long> mtsLightIds)
      throws NoSufficientDataException {
    if (name.length() == 0 || mtsLightIds.size() == 0) {
      throw new NoSufficientDataException("Name or light ids empty");
    }
    List<MtsLight> lights = mtsLightRepository.findAllById(mtsLightIds);
    MtsControlGroup controlGroup = new MtsControlGroup(name, lights);
    return mtsControlGroupRepository.save(controlGroup);
  }

  public List<MtsControlGroup> getAllControlGroups() {
    return mtsControlGroupRepository.findAll();
  }

  @Transactional
  public void setModeToGroupById(long groupId, Long modeId, List<MtsValue> values)
      throws NotFoundException, IndexMissmatchException {
    MtsControlGroup controlGroup = mtsControlGroupRepository.findById(groupId).orElseThrow(
        NotFoundException::new);

    List<Long> lightIds = controlGroup.getLights().stream().map(MtsLight::getId).collect(
        Collectors.toList());
    mtsLightStateService.updateMtsLightStates(lightIds, modeId, values);
  }

  public Optional<MtsControlGroup> getControlGroupById(long groupId){
    return mtsControlGroupRepository.findById(groupId);
  }

}
