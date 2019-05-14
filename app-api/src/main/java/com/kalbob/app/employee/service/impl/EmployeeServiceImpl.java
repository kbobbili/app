package com.kalbob.app.employee.service.impl;

import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
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
