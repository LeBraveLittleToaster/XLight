package de.pschiessle.xlight.xlightserver;

import de.pschiessle.xlight.xlightserver.repositories.AuthTokenRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsControlGroupRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xlightserver.repositories.MtsMoodRepository;
import de.pschiessle.xlight.xlightserver.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseDatabaseTest {
  @Autowired
  MtsControlGroupRepository mtsControlGroupRepository;
  @Autowired
  MtsLightRepository mtsLightRepository;
  @Autowired
  MtsModeRepository mtsModeRepository;
  @Autowired
  MtsMoodRepository mtsMoodRepository;
  @Autowired
  AuthTokenRepository authTokenRepository;
  @Autowired
  UserRepository userRepository;

  @BeforeEach
  public void deleteDatabases(){
    System.out.println("Start deleting database...");
    System.out.println("Deleted MtsControlGroupRepository: " + mtsControlGroupRepository.deleteAll().blockOptional().isPresent());
    System.out.println("Deleted MtsLightRepository: " + mtsLightRepository.deleteAll().blockOptional().isPresent());
    System.out.println("Deleted MtsModeRepository: " + mtsModeRepository.deleteAll().blockOptional().isPresent());
    System.out.println("Deleted MtsMoodRepository: " + mtsMoodRepository.deleteAll().blockOptional().isPresent());
    System.out.println("Deleted AuthTokenRepository: " + authTokenRepository.deleteAll().blockOptional().isPresent());
    System.out.println("Deleted UserRepository: " + userRepository.deleteAll().blockOptional().isPresent());
    System.out.println("End deleting database...");
  }
}
