package com.kalbob.app.data.model;

import java.util.Arrays;

public class DepartmentMother extends ObjectMother {

  public static Department simple() {
    return new Department()
        .setName("Finance")
        ;
  }

  public static Department simpleRandom() {
    return new Department()
        .setName(dataFactory.getItem(Arrays.asList("Sales", "Business", "Marketing", "IT")))
        ;
  }

}
