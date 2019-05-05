package com.kalbob.app.service.config;

import com.kalbob.app.rest.client.EmployeeService;
import com.kalbob.app.data.model.Employee;
import com.kalbob.app.data.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
