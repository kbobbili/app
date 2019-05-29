package com.kalbob.app.project;

import com.kalbob.app.ObjectMother;
import java.util.Arrays;
import java.util.HashSet;

public class ProjectMother extends ObjectMother {

  public static Project simple() {
    return new Project()
        .setName(ProjectName.RED)
        ;
  }

  public static Project complete() {
    return simple()
        .setProjectAssignments(new HashSet<>(Arrays.asList(ProjectAssignmentMother.simple().setProject(
            ProjectMother.simple()))))
        ;
  }

  public static Project simpleRandom() {
    return new Project()
        .setName(dataFactory.getItem(Arrays.asList(ProjectName.values())))
        ;
  }

  public static Project completeRandom() {
    return simpleRandom()
        .setProjectAssignments(new HashSet<>(Arrays.asList(ProjectAssignmentMother.simple().setProject(
            ProjectMother.simpleRandom()))))
        ;
  }
}
