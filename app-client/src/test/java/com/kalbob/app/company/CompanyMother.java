package com.kalbob.app.company;

import com.kalbob.app.ObjectMother;
import com.kalbob.app.department.DepartmentMother;
import java.util.Arrays;

public class CompanyMother extends ObjectMother {

  public static Company simple() {
    return new Company()
        .setName("A Company")
        ;
  }

  public static Company complete() {
    return simple()
        .setDepartments(Arrays.asList(DepartmentMother.simple()))
        ;
  }

  public static Company simpleRandom() {
    return new Company()
        .setName(dataFactory.getItem(Arrays.asList("A Company", "B Company")))
        ;
  }

  public static Company completeRandom() {
    return simpleRandom()
        .setDepartments(Arrays.asList(DepartmentMother.simpleRandom()))
        ;
  }
}
