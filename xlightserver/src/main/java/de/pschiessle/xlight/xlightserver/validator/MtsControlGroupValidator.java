package de.pschiessle.xlight.xlightserver.validator;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import de.pschiessle.xlight.xlightserver.exceptions.IndexMissmatchException;
import java.util.List;
import reactor.core.publisher.Mono;

public class MtsControlGroupValidator {
  public static MtsControlGroup validateInsertControlGroup(String name, List<String> lightIds)
      throws NoSuchFieldException {
    if (name == null || name.length() == 0 || lightIds == null ||lightIds.size() == 0) {
      throw new NoSuchFieldException("Name length zero or lightIds missing");
    }
    return MtsControlGroup.builder()
        .name(name)
        .lightIds(lightIds)
        .lightIds(lightIds)
        .build();
  }

}
