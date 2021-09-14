package de.pschiessle.xlight.xserver.services;

import de.pschiessle.xlight.xserver.components.MtsInput;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MtsModeService {
  final MtsModeRepository mtsModeRepository;

  public MtsModeService(
      MtsModeRepository mtsModeRepository) {
    this.mtsModeRepository = mtsModeRepository;
  }

  public MtsMode createMode(long modeId, List<MtsInput> inputs) throws IllegalArgumentException{
    return mtsModeRepository.save(new MtsMode(modeId, Instant.now().toEpochMilli(), inputs));
  }
}
