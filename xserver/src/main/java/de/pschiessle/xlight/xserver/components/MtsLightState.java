package de.pschiessle.xlight.xserver.components;

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
public class MtsLightState extends BaseEntity{
    @NonNull
    @Column(name = "modeId")
    private long modeId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MtsValue> values;
}
