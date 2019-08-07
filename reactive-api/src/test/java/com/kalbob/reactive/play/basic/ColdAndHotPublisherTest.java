package com.kalbob.reactive.play.basic;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {

  @Test
  public void coldPublisherTest() throws InterruptedException {
    Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
        .delayElements(Duration.ofSeconds(1));//Because i'm delaying the emits, main thread will just finish before the subscribers receive any messages. So, Thread.sleep() the main thread.
    stringFlux
        .subscribe(s -> System.out.println("Subscriber 1 : " + s)); //s1->2 items
    Thread.sleep(2000);
    stringFlux
        .subscribe(s -> System.out.println("Subscriber 2 : " + s));//s1->remaining 4 items & s2->first 4 items
    Thread.sleep(4000);
  }

  @Test
  public void hotPublisherTest() throws InterruptedException {

    Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
        .delayElements(Duration.ofSeconds(1));

    ConnectableFlux<String> connectableFlux = stringFlux.publish();//a hot publishing flux
    connectableFlux.connect();
    connectableFlux.subscribe(s -> System.out.println("Subscriber 1 : " + s));
    Thread.sleep(3000);

    connectableFlux.subscribe(
        s -> System.out.println("Subscriber 2 : " + s)); //hot publishing flux's subscribers continue the stream i.e. each subscriber won't get values from beginning but from continuation.
    Thread.sleep(4000);

  }
}
