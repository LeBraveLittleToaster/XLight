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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MtsInput extends BaseEntity {

  public static enum InputType {HSV, HSVB, SINGLE_DOUBLE, RANGE_2_DOUBLE}

  private InputType inputType;

  private String jsonKey;

  private String uiLabel;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsInput mtsInput = (MtsInput) o;
    return inputType == mtsInput.inputType && Objects.equals(jsonKey, mtsInput.jsonKey)
        && Objects.equals(uiLabel, mtsInput.uiLabel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputType, jsonKey, uiLabel);
  }

}
