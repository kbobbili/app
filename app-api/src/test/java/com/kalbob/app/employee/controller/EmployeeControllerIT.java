package com.kalbob.app.employee.controller;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.kalbob.app.config.rest.BaseRestIT;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.EmployeeMother;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class EmployeeControllerIT extends BaseRestIT {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment environment;

  @Autowired
  private EmployeeService employeeService;

  @MockBean
  private EmployeeRepository employeeRepository;

  @BeforeAll
  public static void beforeAll() {
  }

  @AfterAll
  public static void afterAll() {
  }

  @Test
  public void getAllEmployees() {
    System.out.println(environment.getPropertySources());
    when(employeeRepository.findAll()).thenReturn(
        Arrays.asList(EmployeeMother.simple(), EmployeeMother.complete()));
    List<Employee> employeeList = employeeService.getAllEmployees();
    assertTrue(employeeList.size() == 2);
  }

}
