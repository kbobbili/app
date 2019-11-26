package com.kalbob.code.config.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServiceUnitTestProfile
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
