package com.kalbob.app.employee.service.impl;

import com.kalbob.app.department.repository.DepartmentRepository;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.employee.repository.EmployeeRepository;
import com.kalbob.app.employee.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }
}
