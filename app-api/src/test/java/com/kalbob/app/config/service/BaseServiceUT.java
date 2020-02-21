package com.kalbob.app.config.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith({MockitoExtension.class})
@ActiveProfiles("ut")
public class BaseServiceUT {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @BeforeAll
  public static void beforeAll() {
  }

  @AfterAll
  public static void afterAll() {
  }

  @BeforeEach
  public void beforeEach() {
  }

  @AfterEach
  public void afterEach() {
  }

  @Test
  public void test() {
    logger.info("[::::::BaseServiceUT::::::");
    logger.info("::::::BaseServiceUT::::::]");
  }

}
