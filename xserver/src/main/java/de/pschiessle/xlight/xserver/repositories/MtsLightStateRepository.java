package de.pschiessle.xlight.xserver.repositories;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsLightState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MtsLightStateRepository extends JpaRepository<MtsLightState, Long> {
  MtsLightState findById(long stateId);
}
