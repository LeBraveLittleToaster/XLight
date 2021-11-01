package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsValue;
import java.util.List;
import javax.validation.constraints.NotEmpty;

public record SetLightModeRequest(
    @NotEmpty(message = "No list of values provided") List<MtsValue> values) {

}
