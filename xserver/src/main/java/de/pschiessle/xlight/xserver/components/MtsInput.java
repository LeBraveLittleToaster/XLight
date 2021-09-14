package de.pschiessle.xlight.xserver.components;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mts_inputs")
public class MtsInput extends BaseEntity {
    public static enum InputType {HSV, HSVB, SINGLE_DOUBLE, RANGE_2_DOUBLE}

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "inputType")
    private InputType inputType;

    @NonNull
    @Column(name = "jsonKey")
    private String jsonKey;

    @NonNull
    @Column(name = "uiLabel")
    private String uiLabel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtsInput mtsInput = (MtsInput) o;
        return inputType == mtsInput.inputType && Objects.equals(jsonKey, mtsInput.jsonKey) && Objects.equals(uiLabel, mtsInput.uiLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputType, jsonKey, uiLabel);
    }

}
