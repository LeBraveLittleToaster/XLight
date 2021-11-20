package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsInput;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreateModeRequest(
    @NotNull long modeId,
    @NotNull @Size(min = 3, max = 30) String name,
    @NotEmpty List<MtsInput> inputs) {}
