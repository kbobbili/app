package com.kalbob.app.config.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.Metamodel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Rollback(false)
@Import(BaseRepositoryITConfiguration.class)
@ActiveProfiles("it")
public class BaseRepositoryIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment environment;

  @Autowired
  private EntityManager entityManager;

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
    Metamodel metamodel = entityManager.getMetamodel();
    List<String> tableNames = metamodel.getManagedTypes().stream()
        .filter(m -> m.getJavaType().getAnnotation(Table.class) != null)
        .map(m -> m.getJavaType().getAnnotation(Table.class).name())
        .collect(Collectors.toList());
    entityManager.flush();
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
    tableNames.forEach(tableName ->
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Test
  public void test() {
    logger.info("[::::::BaseRepositoryIT::::::");
    logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
    logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
    logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
    logger.info("spring.datasource.url: {}", environment.getProperty("spring.datasource.url"));
    logger.info("spring.autoconfigure.exclude: {}",
        environment.getProperty("spring.autoconfigure.exclude"));
    logger.info("a: {}", environment.getProperty("a"));
    String[] beans = applicationContext.getBeanDefinitionNames();
    logger.info("Total bean count: " + Arrays.stream(beans)
        .count());//108 DataJpaTest beans vs 517 SpringBootTest beans.
    logger.info("::::::BaseRepositoryIT::::::]");
  }

}
