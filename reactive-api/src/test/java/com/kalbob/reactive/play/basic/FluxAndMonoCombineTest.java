package com.kalbob.reactive.play.basic;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class FluxAndMonoCombineTest {

  @Test
  public void merge() {

    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");

    Flux<String> mergedFlux = Flux.merge(flux1, flux2);

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();

  }

  @Test
  public void mergeWithDelay() {

    Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
    Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

    Flux<String> mergedFlux = Flux.merge(flux1, flux2);

    //the order of the received items can change.
    //this subscriber D, A, E, B, F, C
    mergedFlux
        .subscribe(
            System.out::println,
            (e) -> System.err.println("Exception is " + e),
            () -> System.out.println("Completed")
        );

    //this subscriber A, D, B, E, C, F
    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNextCount(6)
        //.expectNext("A","B","C","D","E","F")
        .verifyComplete();

  }

  @Test
  public void concat() {

    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");

    Flux<String> mergedFlux = Flux.concat(flux1, flux2);//sequentially subscribing to the sources. read docs.

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();

  }

  @Test
  public void virtualTime() {

    Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
    Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
    Flux<String> mergedFlux1 = Flux.concat(flux1, flux2);
    StepVerifier.create(mergedFlux1.log())
        .expectSubscription()
        .expectNext("A","B","C","D","E","F")
        .verifyComplete();


    VirtualTimeScheduler.getOrSet();//Virtualizing the Duration

    flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
    flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
    Flux<String> mergedFlux2 = Flux.concat(flux1, flux2);
    StepVerifier.withVirtualTime(() -> mergedFlux2.log())
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(6))
        .expectNext("A","B","C","D","E","F")
        .verifyComplete();
  }

  @Test
  public void zip() {

    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");

    Flux<String> mergedFlux = Flux.zip(flux1, flux2, (t1, t2) -> {
      return t1.concat(t2); // AD, BE, CF
    }); //A,D : B,E : C:F

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("AD", "BE", "CF")
        .verifyComplete();

  }
}
