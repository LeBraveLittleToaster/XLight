package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MtsControlGroup extends BaseEntity {

  private String controlGroupId;
  private String name;

  private List<String> lightIds;

}
