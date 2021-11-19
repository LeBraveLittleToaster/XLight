package de.pschiessle.xlight.xlightserver.controller.requests;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateControlgroupRequest(
    @NotNull String name,
                                        @NotEmpty List<String> mtsLightIds) {

}
