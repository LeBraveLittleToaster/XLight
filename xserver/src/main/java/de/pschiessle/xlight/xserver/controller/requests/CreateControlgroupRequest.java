package de.pschiessle.xlight.xserver.controller.requests;

import java.util.List;

public class CreateControlgroupRequest {
  public final String name;
  public final List<Long> mtsLightIds;

  public CreateControlgroupRequest(String name, List<Long> mtsLightIds) {
    this.name = name;
    this.mtsLightIds = mtsLightIds;
  }
}
