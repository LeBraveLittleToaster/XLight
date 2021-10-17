package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsMood;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MtsMoodRepository extends ReactiveMongoRepository<MtsMood, String> {

}
