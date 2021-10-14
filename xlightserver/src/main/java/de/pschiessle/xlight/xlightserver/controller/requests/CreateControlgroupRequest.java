package de.pschiessle.xlight.xlightserver.controller.requests;

import java.util.List;

public record CreateControlgroupRequest(String name,
                                        List<Long> mtsLightIds) {

}
