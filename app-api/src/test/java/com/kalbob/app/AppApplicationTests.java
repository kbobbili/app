package com.kalbob.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void test(){
		List<String> l = Arrays.asList("a", "b");
		l = Collections.singletonList("a");
		l.add("c");
		System.out.println(l);
	}


}

