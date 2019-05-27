package com.kalbob.app.employee;

import com.kalbob.app.ObjectMother;
import com.kalbob.app.department.DepartmentMother;
import com.kalbob.app.project.ProjectMother;
import java.util.Arrays;
import java.util.HashSet;

public class EmployeeMother extends ObjectMother {

  public static Employee simple() {
    return new Employee()
        .setFirstName("Kalyan")
        .setLastName("Bobbili")
        .setSalary(5000d)
        ;
  }

  public static Employee complete() {
    return simple()
        .setDepartment(DepartmentMother.simple())
        .setProjects(new HashSet<>(Arrays.asList(ProjectMother.simple())))
        .setManager(EmployeeMother.simple())
        .setReportees(new HashSet<>(Arrays.asList(EmployeeMother.simple(), EmployeeMother.simple())))
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
        .setProjects(new HashSet<>(Arrays.asList(ProjectMother.simpleRandom())))
        .setManager(EmployeeMother.simpleRandom())
        .setReportees(new HashSet<>(Arrays.asList(EmployeeMother.simpleRandom(), EmployeeMother.simpleRandom())))
        ;
  }

}
