package de.pschiessle.xlight.xserver.services;

import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.repositories.MtsLightStateRepository;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xserver.validator.MtsLightStateValidator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MtsLightStateService {

  final MtsLightStateRepository mtsLightStateRepository;
  final MtsModeRepository mtsModeRepository;

  public MtsLightStateService(MtsLightStateRepository mtsLightStateRepository,
      MtsModeRepository mtsModeRepository) {
    this.mtsLightStateRepository = mtsLightStateRepository;
    this.mtsModeRepository = mtsModeRepository;
  }

  @Transactional
  public MtsLightState updateMtsLightState(long modeId, List<MtsValue> values)
      throws IndexMissmatchException {
    MtsMode mtsMode = mtsModeRepository.findMtsModeByModeId(modeId);
    if (mtsMode == null) {
      throw new NullPointerException("MtsMode not found for modeId=" + modeId);
    }
    MtsLightState state = MtsLightStateValidator.validateInsertLightState(mtsMode, values);
    return this.mtsLightStateRepository.save(state);
  }

  @Transactional(readOnly = true)
  public Optional<MtsLightState> getMtsLightStateByDbId(long stateId){
    return Optional.of(mtsLightStateRepository.findById(stateId));
  }
}
