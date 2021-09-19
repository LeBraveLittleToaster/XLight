package de.pschiessle.xlight.xserver.repositories;

import de.pschiessle.xlight.xserver.components.MtsLight;
import java.sql.Blob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MtsLightRepository extends JpaRepository<MtsLight, Long> {
  @Modifying
  @Query("update MtsLight light set light.isOn = ?1 where light.id = ?2")
  void setLightIsOn(boolean isOn , long id);

  @Modifying
  @Query("update MtsLight light set light.picture = ?1 where light.id = ?2")
  void setLightPicture(byte[] picture , long id);
}

