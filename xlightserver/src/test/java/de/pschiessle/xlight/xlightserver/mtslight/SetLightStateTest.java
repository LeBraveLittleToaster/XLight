package de.pschiessle.xlight.xlightserver.mtslight;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.pschiessle.xlight.xlightserver.BaseDatabaseTest;
import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import de.pschiessle.xlight.xlightserver.services.MtsLightStateService;
import de.pschiessle.xlight.xlightserver.services.MtsModeService;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

public class SetLightStateTest extends BaseDatabaseTest {

  @Autowired
  MtsModeService mtsModeService;

  @Autowired
  MtsLightService mtsLightService;


  @Autowired
  MtsLightStateService mtsLightStateService;

  @Test
  public void setLightStateTest() throws Throwable {

    MtsLight light = mtsLightService.createLight("n", "l", "mac0", List.of(1L, 2L))
        .blockOptional()
        .orElseThrow(() -> new Throwable("ERROR"));

    MtsMode mode1 = mtsModeService.createMode(1, "N0", List.of(
        new MtsInput(InputType.HSVB, "j1_1", "ui1_1"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "j1_2", "ui1_2")
    )).blockOptional().orElseThrow(() -> new Throwable("ERROR"));

    Optional<MtsLightState> updatedState = mtsLightStateService.updateMtsLightState(
        light.getLightId(),
        mode1.getModeId(),
        List.of(
            new MtsValue(0L, List.of(1.1d, 1.2d, 1.3d, 1.4d)),
            new MtsValue(1L, List.of(2.1d, 2.2d, 2.3d, 2.4d))
        )).blockOptional();

    assert updatedState.isPresent();
    assert updatedState.get().getValues().get(0).getValueId() == 0L;
    assert updatedState.get().getValues().get(1).getValueId() == 1L;
    assert updatedState.get().getValues().get(0).getValues().get(0) == 1.1d;
    assert updatedState.get().getValues().get(1).getValues().get(2) == 2.3d;

    System.out.println(updatedState.get());

    Optional<MtsLight> updatedLight = mtsLightService
        .getLightByLightId(light.getLightId()).blockOptional();
    assert updatedLight.isPresent();
    assertEquals(updatedState.get(), updatedLight.get().getState());
  }

  @Test
  public void testMultipleLightUpdate() throws Throwable {
    MtsLight light0 = mtsLightService.createLight("n0", "l", "mac0", List.of(1L, 2L))
        .blockOptional()
        .orElseThrow(() -> new Throwable("ERROR"));
    MtsLight light1 = mtsLightService.createLight("n1", "l", "mac1", List.of(1L, 2L))
        .blockOptional()
        .orElseThrow(() -> new Throwable("ERROR"));

    MtsMode mode0 = mtsModeService.createMode(1L, "N0", List.of(
        new MtsInput(InputType.SINGLE_DOUBLE, "j1_1", "ui1_1")
    )).blockOptional().orElseThrow(() -> new Throwable("ERROR"));

    double value0 = 1.5d;
    double value1 = 1.5d;
    Flux<Tuple3<String, Long, List<MtsValue>>> updater = Flux.just(
        Tuples.of(light0.getLightId(), mode0.getModeId(),
            List.of(new MtsValue(0L, List.of(value0)))),
        Tuples.of(light1.getLightId(), mode0.getModeId(),
            List.of(new MtsValue(0L, List.of(value1))))
    );

    long timeStart = Instant.now().toEpochMilli();
    List<MtsLightState> updatedStates = mtsLightStateService.updateMtsLightStates(
        updater).collectList().blockOptional().orElseThrow();

    System.out.println("Took time to update: " + ( Instant.now().toEpochMilli() - timeStart) + " millis");

    MtsLight retrievedLight0 = mtsLightService.getLightByLightId(light0.getLightId())
        .blockOptional().orElseThrow();
    MtsLight retrievedLight1 = mtsLightService.getLightByLightId(light1.getLightId())
        .blockOptional().orElseThrow();

    assert Objects.equals(updatedStates.get(0), retrievedLight0.getState());
    assert Objects.equals(updatedStates.get(1), retrievedLight1.getState());

    assert updatedStates.get(0).getValues().get(0).getValues().get(0) == value0;
    assert updatedStates.get(1).getValues().get(0).getValues().get(0) == value1;
  }
}
