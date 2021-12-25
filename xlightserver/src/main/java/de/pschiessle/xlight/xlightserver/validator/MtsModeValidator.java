package de.pschiessle.xlight.xlightserver.validator;

import de.pschiessle.xlight.xlightserver.components.MtsInput;
import de.pschiessle.xlight.xlightserver.components.MtsMode;
import de.pschiessle.xlight.xlightserver.exceptions.NoSufficientDataException;
import java.util.List;
import reactor.core.publisher.Mono;

public class MtsModeValidator {

  /**
   * Checks if the data is valid to be inserted
   *
   * @param modeId human readable, unique modeId, NOT used to request the mode
   * @param name   name of the mode
   * @param inputs all defined variables that could be used with that mode
   * @return MtsMode without uuid (mtsModeId)
   */
  public static Mono<MtsMode> checkDataForMtsMode(long modeId, String name, List<MtsInput> inputs)
       {
    if (name == null || name.length() == 0
        || inputs.size() == 0
        || inputs.stream()
        .anyMatch(e -> e.getJsonKey().length() == 0 || e.getUiLabel().length() == 0)) {
      return Mono.error(new NoSufficientDataException(
          "Either name is null/length zero or inputs are empty or inputs json or ui label is length zero"));
    }
    return Mono.just(MtsMode.builder()
        .modeId(modeId)
        .name(name)
        .inputs(inputs)
        .build());
  }
}
