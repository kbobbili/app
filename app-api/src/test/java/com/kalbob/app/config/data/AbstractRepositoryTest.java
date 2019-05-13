package com.kalbob.app.config.data;

import com.kalbob.app.config.data.util.DbTruncateService;
import java.util.Arrays;
import javax.persistence.EntityManager;
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

@DataTestProfile
public class AbstractRepositoryTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment environment;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private DbTruncateService dbTruncateService;

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

  public void clearDB() {
    dbTruncateService.truncate();
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Test
  public void test(){
    logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
    logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
    logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
    logger.info("spring.datasource.url: {}", environment.getProperty("spring.datasource.url"));
    logger.info("a: {}", environment.getProperty("a"));
    logger.info("b: {}", environment.getProperty("b"));
    String[] beans = applicationContext.getBeanDefinitionNames();
    System.out.println("Total bean count: " + Arrays.stream(beans).sorted().count());
  }

}
