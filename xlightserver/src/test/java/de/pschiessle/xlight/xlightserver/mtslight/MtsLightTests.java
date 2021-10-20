package de.pschiessle.xlight.xlightserver.mtslight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.pschiessle.xlight.xlightserver.BaseDatabaseTest;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;


public class MtsLightTests extends BaseDatabaseTest {

  @Autowired
  MtsLightService mtsLightService;

  @Test
  public void insertMtsLight(){
    MtsLight insertedLight = mtsLightService.createLight("LightName1", "Location1", "mac0",
        List.of(1L, 2L, 3L, 4L)).block();

    assert insertedLight != null;
    assert insertedLight.getLightId() != null;

    Mono<MtsLight> retrievedLight = mtsLightService.getLightByLightId(insertedLight.getLightId());

    assertEquals(true, retrievedLight.hasElement().block());
    assertEquals(insertedLight.getLightId(), Objects.requireNonNull(retrievedLight.block()).getLightId());

    mtsLightService.deleteLightByLightId(Objects.requireNonNull(retrievedLight.block()).getLightId()).block();

    Mono<MtsLight> emptyLight = mtsLightService.getLightByLightId(insertedLight.getLightId());

    assertEquals(false, emptyLight.hasElement().block());
  }
}
