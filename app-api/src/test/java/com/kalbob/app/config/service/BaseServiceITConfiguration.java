package com.kalbob.app.config.service;

import com.kalbob.app.employee.service.EmployeeComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
//@TestPropertySource(properties= {"k1=v1,spring.autoconfigure.exclude=com.kalbob.app.xyz.XyzAutoConfiguration"}, locations = {"classpath:if-you-need-some-other-file.yml"})
public class BaseServiceITConfiguration {

  @Bean
  public EmployeeComponent employeeComponent(){
    return new EmployeeComponent("two");
  }

}