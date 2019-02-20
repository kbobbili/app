package com.kalbob.app.service;

import com.kalbob.app.data.BaseDataTestConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=XyzConfiguration.class)})
@Import({BaseDataTestConfiguration.class})
public class BaseServiceTestConfiguration {
}