package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.MtsManipulator;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MtsManipulatorRepository extends ReactiveMongoRepository<MtsManipulator, String> {

}
