package com.kalbob.app.task;

import com.kalbob.app.ObjectMother;
import com.kalbob.app.employee.EmployeeMother;
import com.kalbob.app.project.ProjectMother;
import java.util.Arrays;

public class TaskMother extends ObjectMother {

  public static Task simple() {
    return new Task()
        .setDescription("Task Desc 1")
        .setStatus(TaskStatus.STARTED)
        ;
  }

  public static Task complete() {
    return simple()
        .setEmployee(EmployeeMother.simple())
        .setProject(ProjectMother.simple())
        ;
  }

  public static Task simpleRandom() {
    return new Task()
        .setDescription(dataFactory.getItem(Arrays.asList("Task Desc 1", "Task Desc 2", "Task Desc 3")))
        .setStatus(dataFactory.getItem(Arrays.asList(TaskStatus.values())))
        ;
  }

  public static Task completeRandom() {
    return simpleRandom()
        .setEmployee(EmployeeMother.simpleRandom())
        .setProject(ProjectMother.simpleRandom())
        ;
  }
}
