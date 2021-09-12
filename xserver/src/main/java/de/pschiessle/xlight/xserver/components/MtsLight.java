package de.pschiessle.xlight.xserver.components;

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
public class MtsLight extends BaseEntity{
    @NonNull
    @Column(name = "name")
    private String name;
    @NonNull
    @Column(name = "location")
    private String location;
    @NonNull
    @Column(name = "mac")
    private String mac;
    @NonNull
    @Column(name = "is_on")
    private boolean isOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mts_lightstates_id", referencedColumnName = "mac")
    private MtsLightState state;
}
