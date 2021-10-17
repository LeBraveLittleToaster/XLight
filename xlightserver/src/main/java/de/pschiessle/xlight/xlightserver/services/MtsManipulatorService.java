package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.repositories.MtsManipulatorRepository;
import org.springframework.stereotype.Service;

@Service
public class MtsManipulatorService {
  final MtsManipulatorRepository mtsManipulatorRepository;

  public MtsManipulatorService(
      MtsManipulatorRepository mtsManipulatorRepository) {
    this.mtsManipulatorRepository = mtsManipulatorRepository;
  }
}
