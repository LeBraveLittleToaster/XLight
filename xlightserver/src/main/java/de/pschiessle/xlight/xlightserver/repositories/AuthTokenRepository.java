package de.pschiessle.xlight.xlightserver.repositories;

import de.pschiessle.xlight.xlightserver.components.AuthToken;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AuthTokenRepository extends ReactiveMongoRepository<AuthToken, String> {
  Mono<AuthToken> getAuthTokenByEmail(String email);
  Mono<AuthToken> getAuthTokenByToken(String token);
}
