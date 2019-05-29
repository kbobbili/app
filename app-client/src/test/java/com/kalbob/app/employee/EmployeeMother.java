package com.kalbob.app.employee;

import com.kalbob.app.ObjectMother;
import com.kalbob.app.department.DepartmentMother;
import com.kalbob.app.project.ProjectAssignmentMother;
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
        .setProjectAssignments(new HashSet<>(Arrays.asList(ProjectAssignmentMother.simple().setEmployee(EmployeeMother.simple()))))
        .setManager(EmployeeMother.simple())
        .setReportees(Arrays.asList(EmployeeMother.simple(), EmployeeMother.simple()))
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
        .setProjectAssignments(new HashSet<>(Arrays.asList(ProjectAssignmentMother.simpleRandom().setEmployee(EmployeeMother.simpleRandom()))))
        .setManager(EmployeeMother.simpleRandom())
        .setReportees(Arrays.asList(EmployeeMother.simpleRandom(), EmployeeMother.simpleRandom()))
        ;
  }

}
