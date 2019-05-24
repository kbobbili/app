package com.kalbob.app.project.service;

import com.kalbob.app.employee.Employee;
import java.util.List;

public interface EmployeeService {

  Employee findById(Long id);
  List<Employee> getAllEmployees();
}
