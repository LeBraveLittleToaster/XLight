package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class MtsLightState extends BaseEntity {

  @NotNull
  private long modeId;

  @NotEmpty
  private List<MtsValue> values;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsLightState that = (MtsLightState) o;
    return modeId == that.modeId && Objects.equals(values, that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modeId, values);
  }
}
