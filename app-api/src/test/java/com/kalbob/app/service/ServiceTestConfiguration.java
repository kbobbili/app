package com.kalbob.app.service;

import com.kalbob.app.data.DataTestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//@EnableAutoConfiguration
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=XyzConfiguration.class)})
@Import({DataTestConfiguration.class})
public class ServiceTestConfiguration {

}