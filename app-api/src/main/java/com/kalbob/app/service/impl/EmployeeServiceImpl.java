package com.kalbob.app.service.impl;

import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.repository.EmployeeRepository;
import com.kalbob.app.rest.client.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
