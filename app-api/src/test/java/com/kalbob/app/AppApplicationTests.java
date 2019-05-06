package com.kalbob.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppApplicationTests {

  @Test
  public void contextLoads() {
  }

  @Test
  public void test() {
    List<String> l = Arrays.asList("a", "b");
    l = Collections.singletonList("a");
    l.add("c");
    System.out.println(l);
  }


}

