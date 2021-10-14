package de.pschiessle.xlight.xlightserver.validator;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import de.pschiessle.xlight.xlightserver.components.MtsLightState;
import de.pschiessle.xlight.xlightserver.exceptions.NoSufficientDataException;
import java.util.ArrayList;
import java.util.List;

public class MtsLightValidator {

  public static MtsLight validateAddLightObj(String name, String location, String mac,
      List<Long> supportedModes) throws NoSufficientDataException {
    if (name.equals("") || location.equals("") || mac.equals("")
        || supportedModes == null) {
      throw new NoSufficientDataException("Name, Location or mac address empty");
    }
    MtsLight light = MtsLight
        .builder()
        .isOn(false)
        .name(name)
        .mac(mac)
        .supportedModes(supportedModes)
        .location(location)
        .controlGroupIds(List.of())
        .build();

    MtsLightState state = new MtsLightState();
    state.setModeId(-1);
    state.setValues(new ArrayList<>());
    light.setState(state);
    return light;
  }

}
