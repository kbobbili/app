package com.kalbob.app.data.model;

public class DepartmentMother {

    public static Department.DepartmentBuilder simple(){
        return Department.builder()
                .name("Finance")
                ;
    }

}
