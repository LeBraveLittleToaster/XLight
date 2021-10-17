package de.pschiessle.xlight.xlightserver.controller.requests;

import java.util.List;

public record CreateLightRequest(String name, String location,
                                 String mac,
                                 List<Long> supportedModes) {

}
