package de.pschiessle.xlight.xlightserver;

import de.pschiessle.xlight.xlightserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TestDatabaseClearer {

  @Autowired
  MtsControlGroupRepository mtsControlGroupRepository;

  @Autowired
  MtsLightRepository mtsLightRepository;

  @Autowired
  MtsModeRepository mtsModeRepository;

  public void deleteAllDataInRepositories(){
    Mono.zip(
        mtsControlGroupRepository.deleteAll(),
        mtsLightRepository.deleteAll(),
        mtsModeRepository.deleteAll()
    ).block();
  }

}
