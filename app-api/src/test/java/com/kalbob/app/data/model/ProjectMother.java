package com.kalbob.app.data.model;

import java.util.Arrays;

public class ProjectMother extends ObjectMother {

  public static Project simple() {
    return new Project()
        .setName("Project RED")
        ;
  }

  public static Project complete() {
    return simple()
        .setEmployees(Arrays.asList(EmployeeMother.simple()))
        ;
  }

  public static Project simpleRandom() {
    return new Project()
        .setName(dataFactory.getItem(Arrays.asList("Project Blue", "Project Green")))
        ;
  }

  public static Project completeRandom() {
    return simpleRandom()
        .setEmployees(Arrays.asList(EmployeeMother.simpleRandom()))
        ;
  }
}
