package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsMode;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MtsModeRepository extends ReactiveMongoRepository<MtsMode, String> {
  Mono<Void> deleteMtsModeByMtsModeId(String mtsModeId);
  Mono<MtsMode> findByMtsModeId(String mtsModeId);
  Mono<MtsMode> findByModeId(long modeId);

}
