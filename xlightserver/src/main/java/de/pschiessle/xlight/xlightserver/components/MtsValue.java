package de.pschiessle.xlight.xlightserver.components;

import java.math.BigDecimal;
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
public class MtsValue extends BaseEntity {

  private Long valueId;

  private List<Double> values;

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    MtsValue mtsValue = (MtsValue) o;
    return Objects.equals(valueId, mtsValue.valueId) && Objects.equals(values, mtsValue.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valueId, values);
  }
}
