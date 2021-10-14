package de.pschiessle.xlight.xlightserver.mtslight;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MtsLightTests {

  @Autowired
  MtsLightService mtsLightService;

  @Test
  public void insertMtsLight(){
    mtsLightService.createLight()
  }
}
