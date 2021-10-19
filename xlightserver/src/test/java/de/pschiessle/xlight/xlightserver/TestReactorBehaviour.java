package de.pschiessle.xlight.xlightserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

public class TestReactorBehaviour {
  @Test
  public void testErrorBehaviour(){
    Mono<String> stringMono = Mono
        .just("Hello World")
        .flatMap(Mono::just)
        .flatMap(e -> true ? Mono.error(new Throwable("Lul")): Mono.just("lol"))
        .flatMap(Mono::just)
        .switchIfEmpty(Mono.empty())
        .onErrorResume(error -> Mono.just("THIS ISCH ERROR"));

    System.out.println(stringMono.block());

  }
}
