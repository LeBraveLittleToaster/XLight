package de.pschiessle.xlight.xlightserver.mtscontrolgroup;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsInput.InputType;
import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
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
import java.util.stream.Stream;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    Optional<List<MtsLightState>> updateLightStates = mtsControlGroupService.setModeToGroupById(
        mtsControlGroup.get().getControlGroupId(),
        mtsModeOptional.get().getMtsModeId(),
        List.of(
            new MtsValue(0L, List.of(0.5)),
            new MtsValue(1L, List.of(0d, 1d))
        )
    ).blockOptional();

    assert updateLightStates.isPresent();
    assert lightList.size() == updateLightStates.get().size();

    Flux<MtsLight> mtsLightFlux = Flux.mergeSequential(lightList.stream()
            .map(MtsLight::getLightId)
            .map(mtsLightService::getLightByLightId).collect(Collectors.toList()))
        .switchIfEmpty(Flux.error(new Throwable("Failed to load lights")))
        .onErrorResume(Flux::error);

    List<MtsLight> mtsLights = mtsLightFlux.filter(
            light -> updateLightStates.get().stream()
                .anyMatch(state -> Objects.equals(light.getState(), state))).collectList()
        .blockOptional().orElseThrow();

    assert mtsLights.size() == updateLightStates.get().size();

  }
}
