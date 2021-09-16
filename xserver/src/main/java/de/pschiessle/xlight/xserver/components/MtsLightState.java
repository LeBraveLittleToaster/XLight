package de.pschiessle.xlight.xserver.components;

import java.util.Objects;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mts_lightstates")
public class MtsLightState extends BaseEntity {

  @Column(name = "modeId")
  private long modeId;

  @OneToMany(cascade = CascadeType.ALL)
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
