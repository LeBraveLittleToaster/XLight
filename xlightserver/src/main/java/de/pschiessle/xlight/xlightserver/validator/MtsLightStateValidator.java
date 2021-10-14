package de.pschiessle.xlight.xlightserver.validator;

import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.components.MtsValue;
import de.pschiessle.xlight.xlightserver.exceptions.IndexMissmatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import reactor.core.publisher.Mono;

public class MtsLightStateValidator {

  public static MtsLightState validateInsertLightState(MtsMode mtsMode, List<MtsValue> values)
      throws IndexMissmatchException {

    if (!isModeAndValueIndexesEqual(mtsMode, values)) {
      throw new IndexMissmatchException("MtsValue ids and mode input ids don´t match!");
    }
    MtsLightState state = new MtsLightState();
    state.setValues(values);
    state.setModeId(mtsMode.getModeId());
    return state;
  }

  private static boolean isModeAndValueIndexesEqual(MtsMode mtsMode, List<MtsValue> values) {
    List<Long> valuesIds = values.stream().map(MtsValue::getValueId).collect(Collectors.toList());
    List<Long> modeInputIds = Stream.iterate(0L, n -> n + 1)
        .limit(mtsMode.getInputs() == null ? 0 : mtsMode.getInputs().size())
        .collect(Collectors.toList());
    return valuesIds.containsAll(modeInputIds);
  }
}
