package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

/**
 * Multiple {@link de.pschiessle.xlight.xlightserver.components.MtsLight} are grouped via there
 * lightIds to assign a Mode to multiple lightIds at once
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MtsControlGroup extends BaseEntity {

  /**
   * Unique id
   */
  private String controlGroupId;
  /**
   * non unique name, shown to user
   */
  @NotNull
  @Size(min = 5, max = 30)
  private String name;

  /**
   * unique {@link de.pschiessle.xlight.xlightserver.components.MtsLight} lightId field
   */
  @NotEmpty
  private List<String> lightIds;

  public MtsControlGroup addLightId(String lightId) {
    if (lightIds == null) {
      lightIds = List.of(lightId);
    } else if (!lightIds.contains(lightId)) {
      lightIds.add(lightId);
    }
    return this;
  }

  public MtsControlGroup removeLightId(String lightId) {
    if (lightIds != null) {
      lightIds.remove(lightId);
    }
    return this;
  }
}
