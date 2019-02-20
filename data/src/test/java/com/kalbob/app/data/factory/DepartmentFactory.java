package com.kalbob.app.data.factory;

import com.kalbob.app.data.model.Address;
import com.kalbob.app.data.model.Department;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("test")
@Service
public class DepartmentFactory {

    @Autowired
    private DataFactory dataFactory;

    public Department getDepartment() {
        Address address = Address.builder()
                .aptNum(dataFactory.getNumberText(3))
                .street(dataFactory.getStreetName())
                .city(dataFactory.getCity())
                .state(dataFactory.getItem(Arrays.asList("CA", "NY", "AZ")))
                .zipCode(dataFactory.getNumberText(5))
                .build();
        return Department.builder()
                .name(dataFactory.getName())
                .address(address)
                .build();
    }

    public List<Department> getListOfDepartments(int n) {
        List<Department> departmentList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            departmentList.add(getDepartment());
        }
        return departmentList;
    }

    public Department getDepartmentWithId(){
        Department department = getDepartment();
        department.setId(Long.valueOf(dataFactory.getNumberBetween(1,1000)));
        return department;
    }

    public List<Department> getListOfDepartmentsWithIds(int n) {
        List<Department> departmentList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            departmentList.add(getDepartmentWithId());
        }
        return departmentList;
    }
}
