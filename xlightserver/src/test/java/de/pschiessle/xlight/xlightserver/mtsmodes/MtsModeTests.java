package de.pschiessle.xlight.xlightserver.mtsmodes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.pschiessle.xlight.xlightserver.TestDatabaseClearer;
import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xlightserver.services.MtsModeService;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest
public class MtsModeTests {

  @Autowired
  MtsModeService mtsModeService;

  @Autowired
  TestDatabaseClearer testDatabaseClearer;

  @BeforeEach
  public void clearDatabase() {
    // clear database
    testDatabaseClearer.deleteAllDataInRepositories();
  }

  @Test
  public void storeAndRetrieveDeleteTest() {

    Optional<MtsMode> failedMode = mtsModeService
        .createMode(0, "name", new LinkedList<>())
        .onErrorResume(x -> {
          assert x instanceof NoSufficientDataException;
          System.out.println("Error occured as expected, Error is type NoSufficiantData");
          return Mono.empty();
        }).blockOptional();

    failedMode.ifPresent(e -> fail());

    List<MtsInput> initMtsInputs = List.of(
        new MtsInput(InputType.HSVB, "J1", "Ui1"),
        new MtsInput(InputType.SINGLE_DOUBLE, "J2", "Ui2"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "J3", "Ui3")
    );

    MtsMode storedMtsMode = mtsModeService.createMode(0, "name", initMtsInputs).block();

    assert storedMtsMode != null;

    MtsMode retrievedMtsMode = mtsModeService.getModeByMtsModeId(storedMtsMode.getMtsModeId())
        .block();

    assertEquals(storedMtsMode, retrievedMtsMode);

    assert retrievedMtsMode != null;

    mtsModeService.deleteByMtsModeId(storedMtsMode.getMtsModeId()).blockOptional();

    Optional<MtsMode> mtsMode = mtsModeService.getModeByMtsModeId(storedMtsMode.getMtsModeId())
        .blockOptional();
    assertTrue(mtsMode.isEmpty());

  }
}
