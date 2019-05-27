package com.kalbob.app.project;

import com.kalbob.app.ObjectMother;
import com.kalbob.app.employee.EmployeeMother;
import java.time.LocalDateTime;

public class ProjectAssignmentMother extends ObjectMother {

  public static ProjectAssignment simple() {
    return new ProjectAssignment()
        .setIsCurrent(true)
        .setJoinedDate(LocalDateTime.now())
        ;
  }

  public static ProjectAssignment complete() {
    return simple()
        .setEmployee(EmployeeMother.simple())
        .setProject(ProjectMother.simple())
        ;
  }

  public static ProjectAssignment simpleRandom() {
    ProjectAssignment pa = new ProjectAssignment();
    if (dataFactory.chance(85)) {
      pa.setIsCurrent(true);
      pa.setJoinedDate(LocalDateTime.now());
    } else {
      pa.setIsCurrent(false);
      pa.setLeftDate(LocalDateTime.now());
    }
    return pa;
  }

  public static ProjectAssignment completeRandom() {
    return simpleRandom()
        .setEmployee(EmployeeMother.simpleRandom())
        .setProject(ProjectMother.simpleRandom())
        ;
  }
}
