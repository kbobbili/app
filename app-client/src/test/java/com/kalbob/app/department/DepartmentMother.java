package com.kalbob.app.department;

import com.kalbob.app.ObjectMother;

public class DepartmentMother extends ObjectMother {

  public static Department simple() {
    return new Department()
        .setType(DepartmentType.FINANCE)
        ;
  }

  public static Department complete() {
    return simple()
        .setAddress(AddressMother.simple())
        //.setEmployees(new HashSet<>(Arrays.asList(EmployeeMother.simple(), EmployeeMother.simple())))
        //.setProjects(new HashSet<>(Arrays.asList(ProjectMother.simple(), ProjectMother.simple())))
        ;
  }

  public static Department simpleRandom() {
    return new Department()
        .setType(dataFactory.getItem(DepartmentType.values()))
        ;
  }

  public static Department completeRandom() {
    return simpleRandom()
        .setAddress(AddressMother.simpleRandom())
        //.setEmployees(new HashSet<>(Arrays.asList(EmployeeMother.simpleRandom(), EmployeeMother.simpleRandom())))
        //.setProjects(new HashSet<>(Arrays.asList(ProjectMother.simpleRandom(), ProjectMother.simpleRandom())))
        ;
  }

}
