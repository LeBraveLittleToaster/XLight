package de.pschiessle.xlight.xlightserver.controller.requests;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public record CreateControlgroupRequest(
    @NotBlank(message = "No name provided") @NotEmpty(message = "To short name") String name,
    @NotEmpty(message = "Lightids empty") List<String> mtsLightIds) {

}
