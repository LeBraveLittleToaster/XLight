package de.pschiessle.xlight.xlightserver.mtsmodes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.services.MtsModeService;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest
public class MtsModeTests {

  @Autowired
  MtsModeService mtsModeService;

  @Test
  public void storeAndRetrieveDeleteTest() throws InterruptedException {

    Mono<MtsMode> failedMode = mtsModeService.createMode(0, "name", new LinkedList<>());
    failedMode.blockOptional().ifPresent(e -> fail());

    List<MtsInput> initMtsInputs = List.of(
        new MtsInput(InputType.HSVB, "J1", "Ui1"),
        new MtsInput(InputType.SINGLE_DOUBLE, "J2", "Ui2"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "J3", "Ui3")
    );

    MtsMode storedMtsMode = null;

    storedMtsMode = mtsModeService.createMode(0, "name", initMtsInputs).block();
    System.out.println("Stored: " + Objects.requireNonNull(storedMtsMode));

    MtsMode retrievedMtsMode = mtsModeService.getModeByMtsModeId(storedMtsMode.getMtsModeId())
        .block();

    assertEquals(storedMtsMode, retrievedMtsMode);

    assert retrievedMtsMode != null;
    //mtsModeService.deleteById(retrievedMtsMode.getId()).block();

    MtsMode finalStoredMtsMode = storedMtsMode;
    mtsModeService.deleteByMtsModeId(storedMtsMode.getMtsModeId());

    Optional<MtsMode> mtsMode = mtsModeService.getModeByMtsModeId(storedMtsMode.getMtsModeId())
        .blockOptional();
    assertTrue(mtsMode.isEmpty());

  }
}
