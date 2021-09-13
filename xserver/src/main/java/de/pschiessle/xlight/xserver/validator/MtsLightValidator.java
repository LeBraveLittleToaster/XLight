package de.pschiessle.xlight.xserver.validator;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsLightState;
import de.pschiessle.xlight.xserver.exceptions.NoSufficientDataException;
import java.util.ArrayList;

public class MtsLightValidator {
  public static MtsLight validateAddLightObj(MtsLight light) throws NoSufficientDataException {
    if(light.getName() == null || light.getLocation() == null || light.getMac() == null){
      throw new NoSufficientDataException("Name, Location or mac address empty");
    }
    light.setOn(false);
    MtsLightState state = new MtsLightState();
    state.setModeId(-1);
    state.setValues(new ArrayList<>());
    light.setState(state);
    return light;
  }

}
