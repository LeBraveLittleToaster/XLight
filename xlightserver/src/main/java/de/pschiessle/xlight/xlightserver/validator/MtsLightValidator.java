package de.pschiessle.xlight.xlightserver.validator;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.exceptions.NoSufficientDataException;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Mono;

public class MtsLightValidator {

  public static Mono<MtsLight> validateAddLightObj(String name, String location, String mac,
      List<Long> supportedModes) {
    if (name.equals("") || location.equals("") || mac.equals("")
        || supportedModes == null) {
      return Mono.error(new NoSufficientDataException("Name, Location or mac address empty"));
    }
    MtsLight light = MtsLight
        .builder()
        .isOn(false)
        .name(name)
        .mac(mac)
        .supportedModes(supportedModes)
        .location(location)
        .build();

    MtsLightState state = new MtsLightState();
    state.setModeId(-1);
    state.setValues(new ArrayList<>());
    light.setState(state);
    return Mono.just(light);
  }

}
