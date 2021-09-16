package de.pschiessle.xlight.xserver.components;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mts_modes")
public class MtsMode extends BaseEntity {

    @Column(name = "mode_id", unique = true)
    private long modeId;

    @Column(name = "mode_name")
    private String name;

    @Column(name = "change_date_utc")
    private long changeDateUTC;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MtsInput> inputs;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtsMode mtsMode = (MtsMode) o;
        return modeId == mtsMode.modeId && changeDateUTC == mtsMode.changeDateUTC
            && Objects.equals(name, mtsMode.name) && Objects.equals(inputs,
            mtsMode.inputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modeId, name, changeDateUTC, inputs);
    }
}
