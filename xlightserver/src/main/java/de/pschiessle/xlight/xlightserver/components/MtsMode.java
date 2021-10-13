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
public class MtsMode extends BaseEntity {

  private String mtsModeId;

  private long modeId;

  private String name;

  private long changeDateUTC;

  private List<MtsInput> inputs;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsMode mtsMode = (MtsMode) o;
    return Objects.equals(mtsModeId, mtsMode.getMtsModeId())
        &&  modeId == mtsMode.modeId && changeDateUTC == mtsMode.changeDateUTC
        && Objects.equals(name, mtsMode.name) && Objects.equals(inputs,
        mtsMode.inputs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modeId, name, changeDateUTC, inputs);
  }
}
