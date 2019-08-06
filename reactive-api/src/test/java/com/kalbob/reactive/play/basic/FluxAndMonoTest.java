package com.kalbob.reactive.play.basic;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

  @Test
  public void fluxSubscribe() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        //.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("Finally"))
        .log();
    stringFlux
        .subscribe(
            System.out::println,
            (e) -> System.err.println("Exception is " + e),
            () -> System.out.println("Completed")
        );
  }

  @Test
  public void fluxVerifyComplete() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        .verifyComplete();
  }

  @Test
  public void fluxVerifyError() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .log();
    StepVerifier.create(stringFlux)
        //.expectNextCount(3)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        //.expectError(RuntimeException.class)
        .expectErrorMessage("Exception Occurred")
        .verify();
  }

  @Test
  public void monoVerifyComplete() {
    Mono<String> stringMono = Mono.just("Spring");
    StepVerifier.create(stringMono.log())
        .expectNext("Spring")
        .verifyComplete();
  }

  @Test
  public void monoVerifyError() {
    StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred"))
        .log())
        .expectError(RuntimeException.class)
        .verify();
  }
}
