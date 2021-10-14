package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MtsLight extends BaseEntity {

  private String lightId;

  private String name;

  private String location;

  private String mac;

  private boolean isOn;

  private byte[] picture;

  private List<Long> supportedModes;

  private MtsLightState state;

  List<String> controlGroupIds;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsLight mtsLight = (MtsLight) o;
    return name.equals(mtsLight.name) && location.equals(mtsLight.location)
        && mac.equals(mtsLight.mac);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location, mac);
  }
}
