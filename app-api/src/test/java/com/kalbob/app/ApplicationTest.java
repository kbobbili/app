package com.kalbob.app;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootTest
public class ApplicationTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConfigurableEnvironment environment;

  @Test
  public void contextLoads() {
  }

  @Test
  public void test() {

    logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
    logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
    logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
    logger.info("a: {}", environment.getProperty("a"));
    logger.info("b: {}", environment.getProperty("b"));
  }

}

