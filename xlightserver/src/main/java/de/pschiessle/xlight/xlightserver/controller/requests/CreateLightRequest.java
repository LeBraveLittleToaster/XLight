package de.pschiessle.xlight.xlightserver.controller.requests;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public record CreateLightRequest(
    @NotBlank(message = "No name provided") @NotEmpty(message = "To short name") String name,
    @NotBlank(message = "No location provided") @NotEmpty(message = "To short location") String location,
    @NotBlank(message = "No mac provided") @NotEmpty(message = "To short mac") String mac,
    @NotEmpty(message = "No list of supported modes provided") List<Long> supportedModes) {

}
