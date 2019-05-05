package com.kalbob.app.data.model;

import java.util.Arrays;

public class EmployeeMother {

    public static Employee.EmployeeBuilder simple(){
        return Employee.builder()
                .name("kalyan")
                .salary(5000d)
                ;
    }

    public static Employee.EmployeeBuilder complete(){
        return Employee.builder()
                .name("kalyan")
                .salary(5000d)
                .projects(Arrays.asList(ProjectMother.simple().build()))
                .department(DepartmentMother.simple().build())
                ;
    }

}
