package de.pschiessle.xlight.xlightserver.components;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class MtsMood extends BaseEntity {

  private String moodId;

  @NotNull
  @Size(min = 3, max = 30)
  private String name;

  @NotEmpty
  private List<MtsManipulator> manipulatorList;
}

