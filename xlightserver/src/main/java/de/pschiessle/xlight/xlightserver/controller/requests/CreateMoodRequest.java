package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateMoodRequest(
    @NotNull String name,
    @NotEmpty List<MtsManipulator> manipulatorList
) {

}
