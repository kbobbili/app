package com.kalbob.code.config.rest;

import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@RestTestProfile
public class BaseRestIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment environment;

  @BeforeAll
  public static void beforeAll() {
  }

  @AfterAll
  public static void afterAll() {
  }

  @BeforeEach
  public void beforeEach() throws Exception {
  }

  @AfterEach
  public void afterEach() throws Exception {
  }

  @Test
  public void test() {
    logger.info("[::::::BaseRestIT::::::");
    logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
    logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
    logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
    logger.info("spring.datasource.url: {}", environment.getProperty("spring.datasource.url"));
    logger.info("spring.autoconfigure.exclude: {}",
        environment.getProperty("spring.autoconfigure.exclude"));
    logger.info("a: {}", environment.getProperty("a"));
    String[] beans = applicationContext.getBeanDefinitionNames();
    logger.info("Total bean count: " + Arrays.stream(beans)
        .count());//164 DataJpaTest beans vs 517 SpringBootTest beans.
    logger.info("::::::BaseRestIT::::::]");
  }

}
