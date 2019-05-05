package com.kalbob.app.service.impl;

import com.kalbob.app.data.config.BaseDataTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@TestPropertySource(properties=
        {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"})
@SpringBootTest
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=XyzConfiguration.class)})
@Import({BaseDataTestConfiguration.class})
public class BaseServiceTestConfiguration {
}