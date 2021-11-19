package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Definition for one real IOT device
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MtsLight extends BaseEntity {

  /**
   * unique id (UUID)
   */
  private String lightId;

  /**
   * non unique name
   */
  private String name;

  /**
   * description of location in real world
   */
  private String location;

  /**
   * system wide unique id, mqtt address message is send to /device/"mac"
   */
  private String mac;

  /**
   * if light should be turned on completely
   */
  private boolean isOn;

  /**
   * Picture of the device
   */
  private byte[] picture;

  /**
   * modeIds which are supported by the device
   */
  private List<Long> supportedModes;

  /**
   * current state in which the light is
   */
  private MtsLightState state;

  /**
   * ids of control group the light is in
   */
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
