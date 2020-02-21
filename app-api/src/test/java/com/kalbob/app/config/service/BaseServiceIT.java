package com.kalbob.app.config.service;

import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeComponent;
import com.kalbob.app.employee.service.EmployeeService;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {EmployeeComponent.class})
//@Import(BaseServiceITConfiguration.class)
@ActiveProfiles("it")
public class BaseServiceIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment environment;

  @Autowired
  private EmployeeComponent employeeComponent;

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
    System.out.println("hellooooo");
    System.out.println(employeeComponent);
    logger.debug("[::::::BaseServiceUT::::::");
    logger.debug(employeeComponent.toString());
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
    logger.debug("::::::BaseServiceUT::::::]");
  }

}
