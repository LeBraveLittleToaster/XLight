package de.pschiessle.xlight.xserver.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

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

  @JsonProperty(value = "isOn") // for deserialization
  @Column(name = "is_on")
  private boolean isOn;

  @Lob
  @Column(name = "picture")
  private byte[] picture;

  @ElementCollection
  @Column(name = "supported_modes")
  private List<Long> supportedModes;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "fk_mts_lights")
  private MtsLightState state;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "mts_light_control_groups_mapping",
      joinColumns = @JoinColumn(name = "light_id"),
      inverseJoinColumns = @JoinColumn(name = "control_group_id"))
  List<MtsControlGroup> controlGroups;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MtsLight mtsLight = (MtsLight) o;
    return name.equals(mtsLight.name) && location.equals(mtsLight.location)
        && mac.equals(mtsLight.mac);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location, mac);
  }
}
