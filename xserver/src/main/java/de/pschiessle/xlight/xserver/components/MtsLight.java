package de.pschiessle.xlight.xserver.components;

import java.util.Objects;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mts_lights")
public class MtsLight extends BaseEntity {

  @NonNull
  @Column(name = "name")
  private String name;

  @NonNull
  @Column(name = "location")
  private String location;

  @NonNull
  @Column(name = "mac")
  private String mac;

  @Column(name = "is_on")
  private boolean isOn;


  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "fk_mts_lights")
  private MtsLightState state;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsLight mtsLight = (MtsLight) o;
    return  name.equals(mtsLight.name) && location.equals(mtsLight.location)
        && mac.equals(mtsLight.mac);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location, mac);
  }
}
