package com.kalbob.reactive.play.basic;

import static reactor.core.scheduler.Schedulers.parallel;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformTest {

  List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

  @Test
  public void map() {

    Flux<String> namesFlux = Flux.fromIterable(names)
        .map(String::toUpperCase)
        .log();

    StepVerifier.create(namesFlux)
        .expectNext("ADAM", "ANNA", "JACK", "JENNY")
        .verifyComplete();

  }

  @Test
  public void mapToLength() {

    Flux<Integer> namesFlux = Flux.fromIterable(names)
        .map(String::length)
        .log();

    StepVerifier.create(namesFlux)
        .expectNext(4, 4, 4, 5)
        .verifyComplete();

  }

  @Test
  public void mapToLength_Repeat() {

    Flux<Integer> namesFlux = Flux.fromIterable(names)
        .map(String::length)
        .repeat(1)
        .log();

    StepVerifier.create(namesFlux)
        .expectNext(4, 4, 4, 5, 4, 4, 4, 5)
        .verifyComplete();

  }

  @Test
  public void filter_map() {

    Flux<String> namesFlux = Flux.fromIterable(names)
        .filter(s -> s.length() > 4)
        .map(String::toUpperCase)
        .log();

    StepVerifier.create(namesFlux)
        .expectNext("JENNY")
        .verifyComplete();

  }

  @Test
  public void flatMap() {

    Flux<String> stringFlux = Flux
        .fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
        .flatMap(s -> { //db or external service call that takes s and returns a flux
          return Flux.fromIterable(convertToList(s)); // A -> A, a & B -> B, b so on...
        })
        .log();

    StepVerifier.create(stringFlux)
        .expectNextCount(12)
        .verifyComplete();
  }

  private List<String> convertToList(String s) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Arrays.asList(s, s.toLowerCase());
  }

  @Test
  public void flatMapParallel() {

    Flux<String> stringFlux = Flux
        .fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) //Flux<String>
        .window(2) //Flux<Flux<String> -> (A,B), (C,D), (E,F)
        .flatMap((s) -> s.map(this::convertToList).subscribeOn(parallel())) // Flux<List<String>
        .flatMap(s -> Flux.fromIterable(s)) //Flux<String>
        .log();

    StepVerifier.create(stringFlux)
        .expectNextCount(12)
        .verifyComplete();
  }

  @Test
  public void flatMapParallel_MaintainOrder() {

    Flux<String> stringFlux = Flux
        .fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) // Flux<String>
        .window(2) //Flux<Flux<String> -> (A,B), (C,D), (E,F)
        .flatMapSequential((s) ->
            s.map(this::convertToList).subscribeOn(parallel()))
        .flatMap(s -> Flux.fromIterable(s)) //Flux<String>
        .log();

    StepVerifier.create(stringFlux)
        .expectNextCount(12)
        .verifyComplete();
  }

}
