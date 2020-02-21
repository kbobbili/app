package com.kalbob.app.employee.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.kalbob.app.Application;
import com.kalbob.app.config.service.BaseServiceUT;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.EmployeeMother;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class EmployeeServiceUT extends BaseServiceUT {

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private EmployeeServiceImpl employeeService;

  @BeforeEach
  public void beforeEach() {
  }

  @Test
  public void getAllEmployees() {
    when(employeeRepository.findAll()).thenReturn(
        Arrays.asList(EmployeeMother.simple(), EmployeeMother.complete()));
    List<Employee> employeeList = employeeService.getAllEmployees();
    assertEquals(2, employeeList.size());
  }

}
