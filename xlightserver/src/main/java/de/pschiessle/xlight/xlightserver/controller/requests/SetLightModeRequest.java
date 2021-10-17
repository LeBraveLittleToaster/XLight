package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsValue;
import java.util.List;

public record SetLightModeRequest(List<MtsValue> values) {

}
