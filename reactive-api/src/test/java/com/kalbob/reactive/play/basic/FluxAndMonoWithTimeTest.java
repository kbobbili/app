package com.kalbob.reactive.play.basic;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoWithTimeTest {

  @Test
  public void infiniteSequence() throws InterruptedException {
    Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100)) //runs on a separate Schedulers.parallel() //Flux.range(0,10000).log(); doesn't need Thread.sleep()
        .log(); // starts from 0 --> ......
    infiniteFlux
        .subscribe((element) -> System.out.println("Value is : " + element));
    Thread.sleep(3000); // required to print an infinite flux
  }


  @Test
  public void infiniteSequenceTest() throws InterruptedException {

    Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100))
        .take(3)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0L, 1L, 2L)
        .verifyComplete();

  }

  @Test
  public void infiniteSequenceMap() throws InterruptedException {

    Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
        .map(l -> new Integer(l.intValue()))
        .take(3)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0, 1, 2)
        .verifyComplete();

  }

  @Test
  public void infiniteSequenceMap_withDelay() throws InterruptedException {

    Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
        .delayElements(Duration.ofSeconds(1))
        .map(l -> new Integer(l.intValue()))
        .take(3)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0, 1, 2)
        .verifyComplete();

  }

}
