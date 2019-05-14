package com.kalbob.app;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class Application {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ApplicationContext applicationContext;

  private final ConfigurableEnvironment environment;

  public Application(ConfigurableEnvironment environment, ApplicationContext applicationContext) {
    this.environment = environment;
    this.applicationContext = applicationContext;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @Profile("local")
  public CommandLineRunner run(ApplicationContext appContext) {
    return args -> {
      logger.info("[::::::Application::::::");
      logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
      logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
      logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
      logger.info("spring.datasource.url: {}", environment.getProperty("spring.datasource.url"));
      logger.info("spring.autoconfigure.exclude: {}", environment.getProperty("spring.autoconfigure.exclude"));
      logger.info("a: {}", environment.getProperty("a"));
      logger.info("b: {}", environment.getProperty("b"));
      String[] beans = applicationContext.getBeanDefinitionNames();
      logger.info("Total bean count: " + Arrays.stream(beans).count());//517 beans.
      logger.info("::::::Application::::::]");
    };
  }

}

