package de.pschiessle.xlight.xlightserver.controller.requests;

import javax.validation.constraints.NotNull;

public record SetIsOnRequest(
   @NotNull boolean isOn,
   @NotNull String lightId
) {}
