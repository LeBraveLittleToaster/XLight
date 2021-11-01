package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public record CreateMoodRequest(
    @NotBlank(message = "No name provided") @NotEmpty(message = "To short name") String name,
    @NotEmpty(message = "No list of manipulators provided") List<MtsManipulator> manipulatorList
) {

}
