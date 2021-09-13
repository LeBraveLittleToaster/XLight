package de.pschiessle.xlight.xserver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xserver.services.MtsLightService;
import de.pschiessle.xlight.xserver.services.MtsLightStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XserverApplicationTests {

  @Autowired
  private MtsLightService mtsLightService;
  @Autowired
  private MtsLightStateService mtsLightStateService;
  @Autowired
  MtsLightRepository mtsLightRepository;

  @Autowired
  MtsModeRepository mtsModeRepository;


  @BeforeEach
  public void clearDb() {
    System.out.println("Deleting daabase");
    mtsLightRepository.deleteAll();
    mtsModeRepository.deleteAll();
    System.out.println("Deleting daabase");
  }

  @Test
  void contextLoads() {
  }

  @Test
  void savingLightAndStateTest() throws NoSufficientDataException {
    //from API
    MtsLight light = new MtsLight();
    light.setName("SomeName");
    light.setLocation("SomeLocation");
    light.setMac("SomeMac");

    MtsLight insertedLight = mtsLightService.createLight(light);
    System.out.println("LightId=" + light.getId());
    MtsLight foundLight = mtsLightService.getLightById(insertedLight.getId());

    assertEquals(insertedLight, foundLight);
  }

}
