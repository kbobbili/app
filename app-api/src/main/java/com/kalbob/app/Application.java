package com.kalbob.app;

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

  private final ConfigurableEnvironment environment;

  public Application(ConfigurableEnvironment environment) {
    this.environment = environment;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @Profile("local")
  public CommandLineRunner run(ApplicationContext appContext) {
    return args -> {

      logger.info("spring.cloud.config.uri: {}", environment.getProperty("spring.cloud.config.uri"));
      logger.info("spring.profiles.active: {}", environment.getProperty("spring.profiles.active"));
      logger.info("spring.profiles.include: {}", environment.getProperty("spring.profiles.include"));
      logger.info("a: {}", environment.getProperty("a"));
      logger.info("b: {}", environment.getProperty("b"));

    };
  }

}

