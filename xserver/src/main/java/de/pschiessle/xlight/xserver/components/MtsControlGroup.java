package de.pschiessle.xlight.xserver.components;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
@Entity
@Table(name = "mts_controlgroups")
public class MtsControlGroup extends BaseEntity {

  @Column(name = "name")
  private String name;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,
      CascadeType.DETACH})
  @JoinTable(name = "mts_light_control_groups_mapping", joinColumns = @JoinColumn(name = "control_group_id"), inverseJoinColumns = @JoinColumn(name = "light_id"))
  @Exclude
  private List<MtsLight> lights;

}
