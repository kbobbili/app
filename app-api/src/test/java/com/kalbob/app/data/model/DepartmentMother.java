package com.kalbob.app.data.model;

import java.util.Arrays;

public class DepartmentMother {

    public static Department.DepartmentBuilder simple(){
        return Department.builder()
                .name("Finance")
                ;
    }

    public static Department.DepartmentBuilder complete(){
        return simple()
                .address(AddressMother.complete().build())
                .employees(Arrays.asList(EmployeeMother.simple().build()))
                ;
    }

}
