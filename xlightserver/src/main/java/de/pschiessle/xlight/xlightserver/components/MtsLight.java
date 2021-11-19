package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
  @NotNull
  @Size(min = 3, max = 30)
  private String name;

  /**
   * description of location in real world
   */
  @NotNull
  @Size(min = 3, max = 30)
  private String location;

  /**
   * system wide unique id, mqtt address message is send to /device/"mac"
   */
  @NotNull
  @Size(min = 3, max = 30)
  private String mac;

  /**
   * if light should be turned on completely
   */
  @NotNull
  private boolean isOn;

  /**
   * Picture of the device
   */
  private byte[] picture;

  /**
   * modeIds which are supported by the device
   */
  @NotEmpty
  private List<Long> supportedModes;

  /**
   * current state in which the light is
   */
  @NotNull
  private MtsLightState state;


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
