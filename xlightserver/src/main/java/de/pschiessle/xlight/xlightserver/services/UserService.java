package de.pschiessle.xlight.xlightserver.services;

import de.pschiessle.xlight.xlightserver.components.AuthToken;
import de.pschiessle.xlight.xlightserver.components.Role;
import de.pschiessle.xlight.xlightserver.components.User;
import de.pschiessle.xlight.xlightserver.exceptions.NoUserFoundException;
import de.pschiessle.xlight.xlightserver.generators.IdGenerator;
import de.pschiessle.xlight.xlightserver.repositories.AuthTokenRepository;
import de.pschiessle.xlight.xlightserver.repositories.UserRepository;
import java.time.Instant;
import java.util.List;
import javax.security.auth.login.CredentialException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

  private static final long EXPIRATION_TOKEN_MILLIS = 1000 * 60 * 60 * 7;

  private final AuthTokenRepository authTokenRepository;
  private final UserRepository userRepository;

  public UserService(
      AuthTokenRepository authTokenRepository,
      UserRepository userRepository) {
    this.authTokenRepository = authTokenRepository;
    this.userRepository = userRepository;
  }

  public Mono<User> createUser(String email, String password, List<Role> roles) {
    return Mono.just(
            User.builder()
                .userId(IdGenerator.generateUUID())
                .email(email)
                .password(hashPassword(password))
                .roles(roles)
                .build()
        )
        .filterWhen(
            x -> userRepository.getUserByEmail(x.getEmail()).hasElement().map(exist -> !exist))
        .switchIfEmpty(Mono.error(new DuplicateKeyException("Email is already in use")))
        .flatMap(userRepository::insert);
  }

  public Mono<User> getUserByEmailAndPassword(String email, String password) {
    return userRepository.getUserByEmailAndPassword(email, hashPassword(password))
        .map(x -> {
          x.setPassword(null);
          return x;
        });
  }

  public Mono<AuthToken> getAuthTokenByToken(String token){
    return authTokenRepository.getAuthTokenByToken(token);
  }

  public Mono<AuthToken> authenticateUser(String email, String password) {
    final AuthToken tokenCreated = AuthToken.builder()
        .email(email)
        .token(IdGenerator.generateUUID())
        .expirationMillisUtc(Instant.now().plusMillis(EXPIRATION_TOKEN_MILLIS).toEpochMilli())
        .build();

    return Mono.just(tokenCreated)
        .filterWhen(__ ->
            userRepository.getUserByEmailAndPassword(email, hashPassword(password)).hasElement())
        .switchIfEmpty(Mono.error(new CredentialException("Wrong credentials")))
        .zipWith(authTokenRepository.getAuthTokenByEmail(email))
        .flatMap(tokenObjAndDb -> {
          tokenObjAndDb.getT2().setToken(tokenObjAndDb.getT1().getToken());
          tokenObjAndDb.getT2().setExpirationMillisUtc(tokenObjAndDb.getT1()
              .getExpirationMillisUtc());
          return authTokenRepository.save(tokenObjAndDb.getT2());
        }).switchIfEmpty(authTokenRepository.save(tokenCreated));
  }

  public Mono<User> setUserRole(String email, List<Role> roles) {
    return userRepository.getUserByEmail(email)
        .map(user -> {
          user.setRoles(roles);
          return user;
        })
        .switchIfEmpty(Mono.error(new NoUserFoundException("No user with this email found")))
        .flatMap(userRepository::save);
  }


  private String hashPassword(String password) {
    return password;
  }
}
