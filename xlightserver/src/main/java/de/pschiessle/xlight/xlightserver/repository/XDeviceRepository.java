package de.pschiessle.xlight.xlightserver.repository;

import de.pschiessle.xlight.xlightserver.endpoints.XDeviceEndpoints;
import de.pschiessle.xlight.xlightserver.model.XDeviceEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import reactor.core.publisher.Mono;

public interface XDeviceRepository extends ReactiveNeo4jRepository<XDeviceEntity, String> {
    Mono<XDeviceEntity> findOneById(String id);
}
