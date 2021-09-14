package de.pschiessle.xlight.xserver.components;

import java.math.BigDecimal;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mts_values")
public class MtsValue extends BaseEntity {

    @GeneratedValue
    private Long valueId;

    @ElementCollection
    @Column(name = "values")
    private List<BigDecimal> values;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtsValue mtsValue = (MtsValue) o;
        return Objects.equals(valueId, mtsValue.valueId) && Objects.equals(values, mtsValue.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueId, values);
    }
}
