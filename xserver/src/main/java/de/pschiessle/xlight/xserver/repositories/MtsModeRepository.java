package de.pschiessle.xlight.xserver.repositories;

import de.pschiessle.xlight.xserver.components.MtsMode;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface MtsModeRepository extends JpaRepository<MtsMode, Long> {
    MtsMode findMtsModeByModeId(long modeId);

    @Transactional
    void deleteByModeIdEquals(long modeId);
}
