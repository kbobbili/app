package com.kalbob.reactive.play.basic;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

  @Test
  public void backPressureTest() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .thenRequest(1)
        .expectNext(1)
        .thenRequest(1)
        .expectNext(2)
        //.verifyComplete() //It's not completed so obviously, expected: onComplete(); actual: onNext(3)
        .thenCancel()
        .verify();

  }

  @Test
  public void backPressure() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    finiteFlux.subscribe((element) -> System.out.println("Element is : " + element)
        , (e) -> System.err.println("Exception is : " + e)
        , () -> System.out.println("Done")
        , (subscription -> subscription.request(2)));

  }

  @Test
  public void backPressure_cancel() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    finiteFlux.subscribe((element) -> System.out.println("Element is : " + element)
        , (e) -> System.err.println("Exception is : " + e)
        , () -> System.out.println("Done")
        , (subscription -> subscription.cancel()));

  }

  @Test
  public void customized_backPressure() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    finiteFlux.subscribe(new BaseSubscriber<Integer>() {
      @Override
      protected void hookOnNext(Integer value) {//will be invoked every time a value is received i.e. when we receive onNext(v)
        System.out.println("Before manually requesting!");
        request(2);//requesting 2 number of entries on every onNext(val)
        System.out.println("Value received is: " + value);
        if (value == 8) {
          cancel();
        }
      }
    });

  }

}
