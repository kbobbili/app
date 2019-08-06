package com.kalbob.reactive.play.basic;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

  @Test
  public void fluxErrorHandling() {

    Flux<String> stringFlux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorResume((e) -> {
          System.out.println("Exception is : " + e);
          return Flux.just("eos");
        });

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C")
        //.expectError(RuntimeException.class)
        //.verify();
        .expectNext("eos")//end of flux
        .verifyComplete();

  }

  @Test
  public void fluxErrorHandling_OnErrorReturn() {

    Flux<String> stringFlux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorReturn("eos");

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectNext("eos")
        .verifyComplete();

  }

  @Test
  public void fluxErrorHandling_OnErrorMap() {

    Flux<String> stringFlux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap((e) -> new CustomException(e));

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectError(CustomException.class)
        .verify();

  }

  @Test
  public void fluxErrorHandling_OnErrorMap_withRetry() {

    Flux<String> stringFlux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap((e) -> new CustomException(e))
        .retry(2);

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectNext("A", "B", "C")
        .expectNext("A", "B", "C")
        .expectError(CustomException.class)
        .verify();

  }

  @Test
  public void fluxErrorHandling_OnErrorMap_withRetryBackoff() {

    Flux<String> stringFlux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap((e) -> new CustomException(e))
        .retryBackoff(2, Duration.ofSeconds(5));

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectNext("A", "B", "C")
        .expectNext("A", "B", "C")
        .expectError(IllegalStateException.class)
        .verify();
  }


}
