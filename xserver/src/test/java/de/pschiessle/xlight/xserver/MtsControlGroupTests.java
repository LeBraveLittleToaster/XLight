package de.pschiessle.xlight.xserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.pschiessle.xlight.xserver.components.BaseEntity;
import de.pschiessle.xlight.xserver.components.MtsControlGroup;
import de.pschiessle.xlight.xserver.components.MtsInput;
import de.pschiessle.xlight.xserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsMode;
import de.pschiessle.xlight.xserver.components.MtsValue;
import de.pschiessle.xlight.xserver.exceptions.IndexMissmatchException;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xserver.repositories.MtsControlGroupRepository;
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
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@SpringBootTest
public class MtsControlGroupTests {

  @Autowired
  private MtsControlGroupService mtsControlGroupService;
  @Autowired
  private MtsLightService mtsLightService;
  @Autowired
  private MtsLightStateRepository mtsLightStateRepository;
  @Autowired
  private MtsLightStateService mtsLightStateService;


  @Autowired
  MtsControlGroupRepository mtsControlGroupRepository;
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
    mtsControlGroupRepository.deleteAll();
    System.out.println("Deleting database");
  }

  @RepeatedTest(value = 2)
  void createLightAndCreateDeleteGroupTest()
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


    mtsControlGroupService.setModeToGroupById(controlGroupAll.getId(), 0L, List.of(new MtsValue(0L, List.of(
        BigDecimal.valueOf(2)))));

    Optional<MtsControlGroup> groupAllTest = mtsControlGroupService.getControlGroupById(
        controlGroupAll.getId());

    assertTrue(groupAllTest.isPresent());

    MtsLight lightById = mtsLightService.getLightById(controlGroupAll.getLights().get(0).getId());

    assertEquals(2L, lightById.getState().getValues().get(0).getValues().get(0).longValue());


    mtsControlGroupService.removeControlGroupById(groupAllTest.get().getId());

    groupAllTest = mtsControlGroupService.getControlGroupById(
        controlGroupAll.getId());

    assertTrue(groupAllTest.isEmpty());

    lightById = mtsLightService.getLightById(lightById.getId());

    mtsLightService.removeLight(lightById.getId());

    Optional<MtsControlGroup> groupAllTest2 = mtsControlGroupService.getControlGroupById(
        controlGroupAll.getId());
    assertTrue(groupAllTest2.isEmpty());
  }
}
