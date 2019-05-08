package com.kalbob.app.data.model;

import java.util.Arrays;

public class EmployeeMother extends ObjectMother {

  public static Employee simple() {
    return new Employee()
        .setFirstName("kalyan")
        .setLastName("bobbili")
        .setSalary(5000d)
        ;
  }

  public static Employee complete() {
    return simple()
        .setDepartment(DepartmentMother.simple())
        .setProjects(Arrays.asList(ProjectMother.simple()))
        .setManager(EmployeeMother.simple())
        .setEmployees(Arrays.asList(EmployeeMother.simple(), EmployeeMother.simple()))
        ;
  }

  public static Employee simpleRandom() {
    return new Employee()
        .setFirstName(faker.name().lastName())
        .setLastName(faker.name().lastName())
        .setSalary(faker.number().randomDouble(0, 1000, 10000))
        ;
  }

  public static Employee completeRandom() {
    return simpleRandom()
        .setDepartment(DepartmentMother.simpleRandom())
        .setProjects(Arrays.asList(ProjectMother.simpleRandom()))
        .setManager(EmployeeMother.simpleRandom())
        .setEmployees(Arrays.asList(EmployeeMother.simpleRandom(), EmployeeMother.simpleRandom()))
        ;
  }

}
