package de.pschiessle.xlight.xserver.services;

import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.repositories.MtsLightStateRepository;
import org.springframework.stereotype.Service;

@Service
public class MtsLightStateService {

  final MtsLightStateRepository mtsLightStateRepository;

  public MtsLightStateService(MtsLightStateRepository mtsLightStateRepository) {
    this.mtsLightStateRepository = mtsLightStateRepository;
  }

  public MtsLightState updateMtsLightState(MtsLightState state) throws Exception {
    return this.mtsLightStateRepository.save(state);
  }
}
