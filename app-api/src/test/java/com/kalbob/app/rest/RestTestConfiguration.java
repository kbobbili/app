package com.kalbob.app.rest;

import com.kalbob.app.service.ServiceTestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//@EnableAutoConfiguration
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=XyzConfiguration.class)})
@Import({ServiceTestConfiguration.class})
public class RestTestConfiguration {

}