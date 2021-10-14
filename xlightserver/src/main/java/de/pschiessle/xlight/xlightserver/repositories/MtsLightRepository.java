package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MtsLightRepository extends ReactiveMongoRepository<MtsLight, String> {
  Mono<MtsLight> findMtsLightByMac(String mac);
  Mono<MtsLight> findMtsLightByLightId(String lightId);
}
