package de.pschiessle.xlight.xserver.services;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xserver.repositories.MtsLightStateRepository;
import de.pschiessle.xlight.xserver.validator.MtsLightValidator;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MtsLightService {

  final MtsLightRepository lightRepository;
  final MtsLightStateRepository mtsLightStateRepository;

  public MtsLightService(MtsLightRepository lightRepository,
      MtsLightStateRepository mtsLightStateRepository) {
    this.lightRepository = lightRepository;
    this.mtsLightStateRepository = mtsLightStateRepository;
  }

  public List<MtsLight> getLights(){
    return lightRepository.findAll();
  }

  public MtsLight createLight(MtsLight mtsLight)
      throws IllegalArgumentException, NoSufficientDataException {
    return lightRepository.save(MtsLightValidator.validateAddLightObj(mtsLight));
  }

  public MtsLight getLightById(long lightId)
      throws EntityNotFoundException, IllegalArgumentException {
    return lightRepository.findById(lightId)
        .orElseThrow(() -> new EntityNotFoundException("No light with id=" + lightId + " found"));
  }

}
