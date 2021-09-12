package de.pschiessle.xlight.xserver.repositories;

import de.pschiessle.xlight.xserver.components.MtsLight;
import de.pschiessle.xlight.xserver.components.MtsValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MtsValueRepository extends JpaRepository<MtsValue, Long> {
}
