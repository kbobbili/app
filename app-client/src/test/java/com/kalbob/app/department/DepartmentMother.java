package com.kalbob.app.department;

import com.kalbob.app.ObjectMother;
import java.util.Arrays;

public class DepartmentMother extends ObjectMother {

  public static Department simple() {
    return new Department()
        .setType(DepartmentType.FINANCE)
        ;
  }

  public static Department simpleRandom() {
    return new Department()
        .setType(dataFactory.getItem(Arrays
            .asList(DepartmentType.SALES, DepartmentType.IT, DepartmentType.HR,
                DepartmentType.MARKETING, DepartmentType.ADVERTISING)))
        ;
  }

}
