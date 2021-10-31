package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsMood;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MtsMoodRepository extends ReactiveMongoRepository<MtsMood, String> {
  Mono<MtsMood> findByMoodId(String moodId);
}
