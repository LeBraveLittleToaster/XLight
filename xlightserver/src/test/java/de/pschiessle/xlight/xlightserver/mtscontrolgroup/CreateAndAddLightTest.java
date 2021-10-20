package de.pschiessle.xlight.xlightserver.mtscontrolgroup;

import de.pschiessle.xlight.xlightserver.BaseDatabaseTest;
import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.services.MtsControlGroupService;
import de.pschiessle.xlight.xlightserver.services.MtsLightService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class CreateAndAddLightTest extends BaseDatabaseTest {

  @Autowired
  MtsControlGroupService mtsControlGroupService;

  @Autowired
  MtsLightService mtsLightService;


  @Test
  public void addAndRemoveLightIdToGroup() {

    Optional<MtsLight> light0 = mtsLightService.createLight("name0", "Location0", "mac0",
        List.of(1L, 2L, 3L, 4L)).blockOptional();
    assert light0.isPresent();
    Optional<MtsLight> light1 = mtsLightService.createLight("name1", "Location1", "mac1",
        List.of(1L, 2L, 3L, 4L)).blockOptional();
    assert light1.isPresent();

    // CREATE CONTROL GROUP

    Optional<MtsControlGroup> controlGroup = mtsControlGroupService.createControlGroup("name0",
        List.of(light0.get().getLightId())).blockOptional();
    assert controlGroup.isPresent();

    // check if created correctly
    Optional<MtsControlGroup> group = mtsControlGroupService.getControlGroupByControlGroupId(
        controlGroup.get().getControlGroupId()).blockOptional();
    assert group.isPresent();
    assert group.get().getLightIds().size() == 1
        && group.get().getLightIds().contains(light0.get().getLightId());

    Optional<MtsControlGroup> updatedGroup = mtsControlGroupService.addLightIdToControlGroup(
        group.get().getControlGroupId(), light1.get().getLightId()).blockOptional();

    assert updatedGroup.isPresent();
    assert updatedGroup.get().getLightIds().size() == 2
        && updatedGroup.get().getLightIds().containsAll(List.of(
        light0.get().getLightId(),
        light1.get().getLightId()
    ));

    Optional<MtsControlGroup> removedLightIdGroup = mtsControlGroupService
        .removeLightIdFromControlGroup(updatedGroup.get().getControlGroupId(),
            light0.get().getLightId())
        .blockOptional();

    assert removedLightIdGroup.isPresent();
    assert Objects.equals(group.get().get_id(), removedLightIdGroup.get().get_id());
    assert Objects.equals(group.get().get_id(), updatedGroup.get().get_id());
    assert removedLightIdGroup.get().getLightIds().size() == 1
        && removedLightIdGroup.get().getLightIds().contains(light1.get().getLightId());

  }
}
