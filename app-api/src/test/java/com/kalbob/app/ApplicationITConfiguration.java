package com.kalbob.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration //or check how @TestConfiguration
//https://reflectoring.io/spring-boot-test/

//@PropertySource("classpath:if-you-need-some-other-file.yml")
//@EnableAutoConfiguration(exclude = XyzConfiguration.class)
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=XyzConfiguration.class)})
//@TestPropertySource(properties = {"spring.autoconfigure.exclude=XyzAutoConfiguration"})
//@TestPropertySource(properties= {"spring.autoconfigure.exclude=com.kalbob.app.xyz.XyzAutoConfiguration"}, locations = {"classpath:if-you-need-some-other-file.yml"})
//@Import({XyzConfiguration.class})
@Import(ApplicationConfiguration.class)
public class ApplicationITConfiguration {

}