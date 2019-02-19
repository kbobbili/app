package com.kalbob.app.data.factory;

import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.Employee;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeFactory {

    @Autowired
    private DataFactory dataFactory;

    public Employee getEmployee() {
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        return Employee.builder()
                .name(dataFactory.getName())
                .salary(Double.valueOf(dataFactory.getNumberBetween(5000, 8000)))
                .address(address)
                .build();
    }

    public List<Employee> getListOfEmployees(int n) {
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            employeeList.add(getEmployee());
        }
        return employeeList;
    }
}
