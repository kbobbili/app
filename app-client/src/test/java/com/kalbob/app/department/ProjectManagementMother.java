package com.kalbob.app.department;

import com.kalbob.app.ObjectMother;
import com.kalbob.app.project.ProjectMother;

public class ProjectManagementMother extends ObjectMother {

  public static ProjectManagement simple() {
    return new ProjectManagement()
        .setRating(3)
        ;
  }

  public static ProjectManagement complete() {
    return simple()
        .setDepartment(DepartmentMother.simple())
        .setProject(ProjectMother.simple())
        ;
  }

  public static ProjectManagement simpleRandom() {
    return new ProjectManagement()
        .setRating(dataFactory.getItem(new Integer[]{1, 2, 3, 4, 5}))
        ;
  }

  public static ProjectManagement completeRandom() {
    return simpleRandom()
        .setDepartment(DepartmentMother.simpleRandom())
        .setProject(ProjectMother.simpleRandom())
        ;
  }
}
