package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsLight;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MtsLightRepository extends ReactiveMongoRepository<MtsLight, String> {
  Mono<MtsLight> findMtsLightByMac(String mac);
  Mono<MtsLight> findMtsLightByLightId(String lightId);
  Flux<MtsLight> findMtsLightsByLightId(List<String> lightIds);
  Mono<Void> deleteByLightId(String lightId);
  Flux<MtsLight> findAllByLightIdIn(List<String> lightIds);
}
