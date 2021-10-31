package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsControlGroup;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MtsControlGroupRepository extends ReactiveMongoRepository<MtsControlGroup, String> {
  Mono<MtsControlGroup> findByControlGroupId(String controlGroupId);
}
