package de.pschiessle.xlight.xlightserver.controller.requests;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateLightRequest(
    @NotNull String name,
    @NotNull String location,
    @NotNull String mac,
    @NotEmpty List<Long> supportedModes) {

}
