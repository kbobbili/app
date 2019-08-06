package com.kalbob.reactive.employee.service.impl;

import com.kalbob.reactive.employee.Employee;
import com.kalbob.reactive.employee.repository.EmployeeRepository;
import com.kalbob.reactive.employee.service.EmployeeService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  public final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }
}
