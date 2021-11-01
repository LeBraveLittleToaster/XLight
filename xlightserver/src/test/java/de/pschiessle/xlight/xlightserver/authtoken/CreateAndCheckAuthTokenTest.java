package de.pschiessle.xlight.xlightserver.authtoken;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.pschiessle.xlight.xlightserver.BaseDatabaseTest;
import de.pschiessle.xlight.xlightserver.components.AuthToken;
import de.pschiessle.xlight.xlightserver.components.Role;
import de.pschiessle.xlight.xlightserver.components.User;
import de.pschiessle.xlight.xlightserver.repositories.AuthTokenRepository;
import de.pschiessle.xlight.xlightserver.services.UserService;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateAndCheckAuthTokenTest extends BaseDatabaseTest {

  public static final Duration DB_QUERY_TIMEOUT = Duration.of(30, ChronoUnit.SECONDS);
  @Autowired
  UserService userService;

  @Autowired
  AuthTokenRepository authTokenRepository;

  @Test
  public void createAuthenticateTest() {
    String email = "mail";
    String password = "password";
    Optional<User> userOpt = userService.createUser(email, password, List.of(Role.USER, Role.ADMIN))
        .blockOptional();

    assert userOpt.isPresent();
    User user = userOpt.get();
    assert Objects.equals(user.getEmail(), email);
    assert Objects.equals(user.getPassword(), password);

    Optional<AuthToken> authTokenOpt = userService.authenticateUser(email, password)
        .blockOptional();

    assert authTokenOpt.isPresent();
    AuthToken authToken = authTokenOpt.get();
    assert Objects.equals(authToken.getEmail(), email);

    Optional<AuthToken> authTokenOpt2 = userService.authenticateUser(email, password)
        .blockOptional();

    assert authTokenOpt2.isPresent();
    AuthToken authToken2 = authTokenOpt2.get();
    assert Objects.equals(authToken2.getEmail(), email);

    assert Objects.equals(authToken.get_id(), authToken2.get_id());
    assert !Objects.equals(authToken.getToken(), authToken2.getToken());

    assert authTokenRepository.findAll().count().block(DB_QUERY_TIMEOUT) == 1L;

    Optional<AuthToken> oldNonPresentAuthtoken = userService.getAuthTokenByToken(authToken.getToken())
        .blockOptional(DB_QUERY_TIMEOUT);

    assert oldNonPresentAuthtoken.isEmpty();

    Optional<AuthToken> retrievedValidAuthtoken = userService.getAuthTokenByToken(authToken2.getToken())
        .blockOptional(DB_QUERY_TIMEOUT);

    assert retrievedValidAuthtoken.isPresent();
    assertEquals(retrievedValidAuthtoken.get(), authToken2);
  }
}
