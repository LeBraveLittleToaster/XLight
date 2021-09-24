package de.pschiessle.xlight.xserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.pschiessle.xlight.xserver.components.BaseEntity;
import de.pschiessle.xlight.xserver.components.MtsControlGroup;
import de.pschiessle.xlight.xserver.components.MtsInput;
import de.pschiessle.xlight.xserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.repositories.MtsLightRepository;
import de.pschiessle.xlight.xserver.repositories.MtsLightStateRepository;
import de.pschiessle.xlight.xserver.repositories.MtsModeRepository;
import de.pschiessle.xlight.xserver.services.MtsControlGroupService;
import de.pschiessle.xlight.xserver.services.MtsLightService;
import de.pschiessle.xlight.xserver.services.MtsLightStateService;
import de.pschiessle.xlight.xserver.services.MtsModeService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@SpringBootTest
class XserverApplicationTests {

  @Autowired
  private MtsControlGroupService mtsControlGroupService;
  @Autowired
  private MtsLightService mtsLightService;
  @Autowired
  private MtsLightStateRepository mtsLightStateRepository;
  @Autowired
  private MtsLightStateService mtsLightStateService;
  @Autowired
  MtsLightRepository mtsLightRepository;


  @Autowired
  MtsModeRepository mtsModeRepository;

  @Autowired
  MtsModeService mtsModeService;


  @BeforeEach
  public void clearDb() {
    System.out.println("Deleting database");
    mtsLightRepository.deleteAll();
    mtsModeRepository.deleteAll();
    mtsLightStateRepository.deleteAll();
    System.out.println("Deleting database");
  }

  @RepeatedTest(value = 2)
  void createLightAndCreateGroupTest()
      throws NoSufficientDataException, NotFoundException, IndexMissmatchException {

    List<MtsLight> lights = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      MtsLight light = new MtsLight();
      light.setName("Name" + i);
      light.setLocation("Loc" + i);
      light.setOn(true);
      light.setMac("Mac" + i);
      light.setSupportedModes(List.of(1L, 2L, 3L));
      lights.add(light);
    }

    List<MtsLight> lightsStore = new ArrayList<>();

    lights.forEach((e) -> {
      try {
        lightsStore.add(mtsLightService.createLight(e));
      } catch (NoSufficientDataException ex) {
        ex.printStackTrace();
      }
    });

    List<MtsMode> modes = new ArrayList<>();
    for(int i = 0; i < 3; i++){
      modes.add(mtsModeService.createMode(i,"Mode" + i, List.of(new MtsInput(InputType.SINGLE_DOUBLE, "k" + i, "ui" + i))));
    }

    MtsControlGroup controlGroupAll = mtsControlGroupService.createControlGroup("NameAll",
        lightsStore.stream().map(BaseEntity::getId).collect(
            Collectors.toList()));

    MtsControlGroup controlGroupFirstTwo = mtsControlGroupService.createControlGroup("NameFirstTwo",
        lightsStore.stream().limit(2).map(BaseEntity::getId).collect(
            Collectors.toList()));

    assertEquals("NameAll", controlGroupAll.getName());
    assertEquals("NameFirstTwo", controlGroupFirstTwo.getName());

    assertEquals(3, controlGroupAll.getLights().size());
    assertEquals(2, controlGroupFirstTwo.getLights().size());

    assertEquals("Name0", controlGroupAll.getLights().get(0).getName());
    assertEquals("Name1", controlGroupAll.getLights().get(1).getName());


    mtsControlGroupService.setModeToGroupById(controlGroupAll.getId(), 0L, List.of(new MtsValue(0L, List.of(BigDecimal.valueOf(2)))));

    Optional<MtsControlGroup> groupAllTest = mtsControlGroupService.getControlGroupById(
        controlGroupAll.getId());

    assertTrue(groupAllTest.isPresent());

    MtsLight lightById = mtsLightService.getLightById(controlGroupAll.getLights().get(0).getId());

    assertEquals(2L, lightById.getState().getValues().get(0).getValues().get(0).longValue());

  }

  @Test
  void savingLightAndStateTest()
      throws NoSufficientDataException, IndexMissmatchException, NotFoundException {
    //from API
    List<Long> supportedModes = List.of(0L, 1L, 2L, 5L);
    MtsLight light = new MtsLight();
    light.setName("SomeName");
    light.setLocation("SomeLocation");
    light.setMac("SomeMac");
    light.setSupportedModes(supportedModes);

    MtsLight insertedLight = mtsLightService.createLight(light);
    System.out.println("LightId=" + light.getId());
    MtsLight foundLight = mtsLightService.getLightById(insertedLight.getId());

    assertEquals(insertedLight, foundLight);

    MtsMode mode0 = mtsModeService.createMode(0, "name0", List.of(
        new MtsInput(InputType.HSVB, "json1", "ui1"),
        new MtsInput(InputType.SINGLE_DOUBLE, "json1_0", "ui1_0"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "json2_0", "ui2_0")
    ));
    System.out.println("Created mode0=" + mode0.toString());
    MtsMode mode1 = mtsModeService.createMode(1, "name1", List.of(
        new MtsInput(InputType.HSVB, "json11", "ui11"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "json11", "ui11"),
        new MtsInput(InputType.SINGLE_DOUBLE, "json21", "ui21"),
        new MtsInput(InputType.SINGLE_DOUBLE, "json31", "ui31")
    ));
    System.out.println("Created mode1=" + mode1.toString());

    MtsLightState state = mtsLightStateService.updateMtsLightState(insertedLight.getId(), 1,
        List.of(
            new MtsValue(0L, generateRandomList(4)),
            new MtsValue(1L, generateRandomList(2)),
            new MtsValue(2L, generateRandomList(1)),
            new MtsValue(3L, generateRandomList(1))
        ));

    Optional<MtsLightState> stateFromDb = mtsLightStateService.getMtsLightStateByDbId(
        state.getId());
    assertTrue(stateFromDb.isPresent());
    System.out.println("COMPARING STATE: ");
    System.out.println(state);
    System.out.println("WITH STATE_FROM_DB");
    System.out.println(stateFromDb.get());
    assertEquals(state.getValues().size(), stateFromDb.get().getValues().size());
    for (int i = 0; i < state.getValues().size(); i++) {
      assertEquals(state.getValues().get(i).getValueId(),
          stateFromDb.get().getValues().get(i).getValueId());
      for (int x = 0; x < state.getValues().get(i).getValues().size(); x++) {
        double vState = state.getValues().get(i).getValues().get(x).doubleValue();
        double vFromDbState = stateFromDb.get().getValues().get(i).getValues().get(x).doubleValue();
        assertTrue(Math.abs(vState - vFromDbState) < 0.01d);
      }
    }

  }

  private List<BigDecimal> generateRandomList(int size) {
    final Random random = new Random();
    return Stream.iterate(0, n -> n + 1)
        .limit(size)
        .map(e -> {
          BigDecimal v = BigDecimal.valueOf(random.nextDouble() * 255);
          System.out.println(v);
          return v;
        })
        .sorted()
        .collect(Collectors.toList());
  }
}
