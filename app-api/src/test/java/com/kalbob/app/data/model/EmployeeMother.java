package com.kalbob.app.data.model;

public class EmployeeMother {

    public static Employee.EmployeeBuilder simple(){
        return Employee.builder()
                .name("kalyan")
                .salary(5000d)
                ;
    }

}
