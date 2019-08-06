package com.kalbob.reactive.play.basic;


import org.junit.jupiter.api.Test;

public class EventLoopTest {

  @Test
  public void noOfProcessors() {

    System.out.println(Runtime.getRuntime().availableProcessors());
  }
}
