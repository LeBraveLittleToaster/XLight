package de.pschiessle.xlight.xlightserver.validator;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.exceptions.IndexMissmatchException;
import java.util.List;
import reactor.core.publisher.Mono;

public class MtsControlGroupValidator {
  public static Mono<MtsControlGroup> validateInsertControlGroup(String name, List<String> lightIds)
  {
    if (name == null || name.length() == 0 || lightIds == null ||lightIds.size() == 0) {
      return Mono.error(new NoSuchFieldException("Missing either name or lightIds"));
    }
    return Mono.just(MtsControlGroup.builder()
        .name(name)
        .lightIds(lightIds)
        .lightIds(lightIds)
        .build());
  }

}
