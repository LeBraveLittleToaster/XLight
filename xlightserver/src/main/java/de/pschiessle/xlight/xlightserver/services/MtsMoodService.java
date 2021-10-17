package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.repositories.MtsMoodRepository;
import org.springframework.stereotype.Service;

@Service
public class MtsMoodService {

  final MtsMoodRepository mtsMoodRepository;

  public MtsMoodService(
      MtsMoodRepository mtsMoodRepository) {
    this.mtsMoodRepository = mtsMoodRepository;
  }
}
