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

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MtsMode extends BaseEntity {

  private String mtsModeId;

  @NotNull
  private Long modeId;

  @NotNull
  @Size(min = 3, max = 30)
  private String name;

  @NotNull
  private long changeDateUTC;

  @NotEmpty
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
