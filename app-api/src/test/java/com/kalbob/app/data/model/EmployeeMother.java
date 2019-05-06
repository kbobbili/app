package com.kalbob.app.data.model;

import java.util.Arrays;
import java.util.HashSet;

public class EmployeeMother extends ObjectMother {

  public static Employee.EmployeeBuilder simple() {
    return Employee.builder()
        .firstName("kalyan")
        .lastName("bobbili")
        .salary(5000d)
        ;
  }

  public static Employee.EmployeeBuilder complete() {
    return simple()
        .projects(new HashSet<>(Arrays.asList(ProjectMother.simple().build())))
        .department(DepartmentMother.simple().build())
        ;
  }

  public static Employee.EmployeeBuilder simpleRandom() {
    return Employee.builder()
        .firstName(faker.name().lastName())
        .lastName(faker.name().lastName())
        .salary(faker.number().randomDouble(0, 1000, 10000))
        ;
  }

  public static Employee.EmployeeBuilder completeRandom() {
    return simpleRandom()
        .projects(new HashSet<>(Arrays.asList(ProjectMother.simpleRandom().build())))
        .department(DepartmentMother.simpleRandom().build())
        ;
  }

}
