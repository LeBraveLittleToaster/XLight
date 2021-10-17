package de.pschiessle.xlight.xlightserver.mtscontrolgroup;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.services.MtsControlGroupService;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import de.pschiessle.xlight.xlightserver.services.MtsModeService;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UpdateControlGroupStateTest {

  @Autowired
  MtsLightService mtsLightService;

  @Autowired
  MtsModeService mtsModeService;

  @Autowired
  MtsControlGroupService mtsControlGroupService;


  @Test
  public void updateMultipleTest() {

    List<MtsLight> lightList = new LinkedList<>();

    for (int i = 0; i < 3; i++) {
      Optional<MtsLight> lightOptional = mtsLightService.createLight("name" + i, "Location" + i,
          "mac" + i,
          List.of(1L, 2L, 3L, 4L)).blockOptional();
      assert lightOptional.isPresent();
      lightList.add(lightOptional.get());
    }

    Optional<MtsControlGroup> mtsControlGroup = mtsControlGroupService.createControlGroup("G1",
        lightList.stream().map(MtsLight::getLightId).collect(
            Collectors.toList())).blockOptional();

    assert mtsControlGroup.isPresent();

    Optional<MtsMode> mtsModeOptional = mtsModeService.createMode(99, "Mode", List.of(
        new MtsInput(InputType.SINGLE_DOUBLE, "l1", "ui1"),
        new MtsInput(InputType.RANGE_2_DOUBLE, "l1", "ui1")
    )).blockOptional();

    assert mtsModeOptional.isPresent();

    Optional<List<String>> updatedListString = mtsControlGroupService.setModeToGroupById(
        mtsControlGroup.get().getControlGroupId(),
        mtsModeOptional.get().getMtsModeId(),
        List.of(
            new MtsValue(0L, List.of(0.5)),
            new MtsValue(1L, List.of(0d, 1d))
        )
    ).blockOptional();

    assert updatedListString.isPresent();
    assert lightList.stream().map(MtsLight::getLightId).collect(Collectors.toList())
        .containsAll(updatedListString.get());

    updatedListString
        .get()
        .forEach(lightId -> {
          Optional<MtsLight> updatedLight = mtsLightService.getLightByLightId(lightId)
              .blockOptional();
          assert updatedLight.isPresent();
          assert Objects.equals(lightId, updatedLight.get().getLightId());
          assert updatedLight.get().getState().getValues().size() == 2;
          assert updatedLight.get().getState().getValues().get(0).getValueId() == 0;
          assert updatedLight.get().getState().getValues().get(0).getValues().get(0) == 0.5;
          assert updatedLight.get().getState().getValues().get(1).getValueId() == 1;
          assert updatedLight.get().getState().getValues().get(1).getValues().get(0) == 0d;
          assert updatedLight.get().getState().getValues().get(1).getValues().get(1) == 1d;
        });

  }
}
