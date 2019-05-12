package com.kalbob.app.employee.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.kalbob.app.config.service.ServiceConfiguration;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {ServiceConfiguration.class})
//@Import({ServiceTestConfiguration.class})
public class EmployeeServiceImplTest {

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
    when(employeeRepository.findAll()).thenReturn(
        Arrays.asList(EmployeeMother.simple(), EmployeeMother.complete()));
    List<Employee> employeeList = employeeService.getAllEmployees();
    assertTrue(employeeList.size() == 2);
  }

}
