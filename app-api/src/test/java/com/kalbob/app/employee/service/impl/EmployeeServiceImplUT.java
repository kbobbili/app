package com.kalbob.app.employee.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.kalbob.app.config.service.BaseServiceUT;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.EmployeeMother;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.project.repository.ProjectRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class EmployeeServiceImplUT extends BaseServiceUT {

  private EmployeeServiceImpl employeeServiceImpl;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ProjectRepository projectRepository;

  @BeforeEach
  public void beforeEach() {
    employeeServiceImpl = new EmployeeServiceImpl(employeeRepository, projectRepository);
  }

  @Test
  public void getAllEmployees() {
    when(employeeRepository.findAll()).thenReturn(
        Arrays.asList(EmployeeMother.simple(), EmployeeMother.complete()));
    List<Employee> employeeList = employeeServiceImpl.getAllEmployees();
    assertEquals(2, employeeList.size());
  }

}
