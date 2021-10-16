package de.pschiessle.xlight.xlightserver.mtslight;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SetLightStateTest {

  @Autowired
  MtsModeService mtsModeService;

  @Autowired
  MtsLightService mtsLightService;

  @Autowired
  MtsLightStateService mtsLightStateService;


  @Test
  public void setLightStateTest() throws Throwable {
    long timeStart = Instant.now().toEpochMilli();

    MtsLight light = mtsLightService.createLight("n", "l", "mac", List.of(1L, 2L)).blockOptional()
        .orElseThrow(() -> new Throwable("ERROR"));

    MtsMode mode1 = mtsModeService.createMode(1, "N0", List.of(
        new MtsInput(InputType.HSVB, "j1_1", "ui1_1"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "j1_2", "ui1_2")
    )).blockOptional().orElseThrow(() -> new Throwable("ERROR"));
    /*
    MtsMode mode2 = mtsModeService.createMode(2, "N0", List.of(
        new MtsInput(InputType.RANGE_2_DOUBLE, "j2_1", "ui2_1"),
        new MtsInput(InputType.SINGLE_DOUBLE, "j2_2", "ui2_2")
    )).blockOptional().orElseThrow(() -> new Throwable("ERROR"));
    MtsMode mode3 = mtsModeService.createMode(3, "N0", List.of(
        new MtsInput(InputType.HSVB, "j3_1", "ui3_1"),
        new MtsInput(InputType.HSV, "j3_2", "ui3_2")
    )).blockOptional().orElseThrow(() -> new Throwable("ERROR"));
    */

    Optional<MtsLightState> updatedState = mtsLightStateService.updateMtsLightState(
        light.getLightId(),
        mode1.getMtsModeId(),
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
}
