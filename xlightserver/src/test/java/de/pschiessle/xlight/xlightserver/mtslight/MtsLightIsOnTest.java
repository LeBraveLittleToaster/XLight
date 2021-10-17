package de.pschiessle.xlight.xlightserver.mtslight;

import de.pschiessle.xlight.xlightserver.BaseDatabaseTest;
import de.pschiessle.xlight.xlightserver.TestDatabaseClearer;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MtsLightIsOnTest extends BaseDatabaseTest {

  @Autowired
  MtsLightService mtsLightService;

  public MtsLightIsOnTest(TestDatabaseClearer testDatabaseClearer) {
    super(testDatabaseClearer);
  }

  @Test
  public void setIsOnTest() {

    // clear database
    getTestDatabaseClearer().deleteAllDataInRepositories();

    Optional<MtsLight> light = mtsLightService.createLight("Name1", "Location1", "Mac1",
        List.of(1L, 2L)).blockOptional();

    assert light.isPresent();

    Optional<MtsLight> updatedLight = mtsLightService.setLightIsOn(light.get().getLightId(), true)
        .blockOptional();

    assert updatedLight.isPresent();
    assert Objects.equals(light.get().get_id(), updatedLight.get().get_id());
    assert updatedLight.get().isOn();

    updatedLight = mtsLightService.setLightIsOn(light.get().getLightId(), false).blockOptional();

    assert updatedLight.isPresent();
    assert Objects.equals(light.get().get_id(), updatedLight.get().get_id());
    assert !updatedLight.get().isOn();
  }

}
