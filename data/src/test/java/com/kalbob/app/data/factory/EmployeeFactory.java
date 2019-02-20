package com.kalbob.app.data.factory;

import com.kalbob.app.data.model.Employee;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeFactory {

    @Autowired
    private DataFactory dataFactory;

    public Employee getEmployee() {

        return Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .build();
    }

    public List<Employee> getListOfEmployees(int n) {
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            employeeList.add(getEmployee());
        }
        return employeeList;
    }

    public Employee getEmployeeWithId(){
        Employee employee = getEmployee();
        employee.setId(Long.valueOf(dataFactory.getNumberBetween(1,1000)));
        return employee;
    }

    public List<Employee> getListOfEmployeesWithIds(int n) {
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            employeeList.add(getEmployeeWithId());
        }
        return employeeList;
    }
}
