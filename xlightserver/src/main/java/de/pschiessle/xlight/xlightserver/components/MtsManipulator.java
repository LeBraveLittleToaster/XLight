package de.pschiessle.xlight.xlightserver.components;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MtsManipulator extends BaseEntity {

  private String manipulatorId;
  private String lightId;
  private MtsLightState state;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsManipulator that = (MtsManipulator) o;
    return Objects.equals(manipulatorId, that.manipulatorId) && Objects.equals(
        lightId, that.lightId) && Objects.equals(state, that.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(manipulatorId, lightId, state);
  }
}
