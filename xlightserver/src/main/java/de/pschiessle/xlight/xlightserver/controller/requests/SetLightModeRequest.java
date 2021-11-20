package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsValue;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record SetLightModeRequest(
    @NotNull long modeId,
    @NotNull String lightId,
    @NotEmpty List<MtsValue> values) {

}
