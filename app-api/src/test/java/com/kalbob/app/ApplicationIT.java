package com.kalbob.app;

import java.util.Arrays;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest //It brings in all of the application context i.e. SpringBootConfiguration (including application.yml overrides); 517 beans & boots up with default properties i.e. like flyway & in-mem h2 urls etc. because i have not defined application.yml
@ActiveProfiles("it")
public class ApplicationIT {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment environment;

  @Test
  public void contextLoads() {
  }

  @Test
  public void test() {
    logger.info("[::::::ApplicationIT::::::");
    MutablePropertySources sources = environment.getPropertySources();
    Iterator<PropertySource<?>> iterator = sources
        .iterator();
    int i = 0;
    while (iterator.hasNext()) {
      logger.info("{} {}", i++, iterator.next().getName());
    }
    logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
    logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
    logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
    logger.info("spring.datasource.url: {}", environment.getProperty("spring.datasource.url"));
    logger.info("spring.autoconfigure.exclude: {}",
        environment.getProperty("spring.autoconfigure.exclude"));
    logger.info("a: {}", environment.getProperty("a"));
    String[] beans = applicationContext.getBeanDefinitionNames();
    logger.info("Total bean count: " + Arrays.stream(beans).count());//517 beans.
    logger.info("::::::ApplicationIT::::::]");
  }

}

