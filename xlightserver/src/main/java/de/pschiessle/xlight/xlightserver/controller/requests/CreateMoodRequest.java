package de.pschiessle.xlight.xlightserver.controller.requests;

import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import java.util.List;

public record CreateMoodRequest(
    String name,
    List<MtsManipulator> manipulatorList
) {

}
