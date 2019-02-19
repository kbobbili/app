package com.kalbob.app.common.utils;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//Below annotations won't work because this configuration is not in test scope
//@TestPropertySource(locations = {"classpath:if-you-need-some-other-file.yml"})
//@TestPropertySource(properties= {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"})
public class BaseServiceTestConfiguration {
}