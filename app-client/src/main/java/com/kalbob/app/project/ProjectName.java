package com.kalbob.code.project;

public enum ProjectName {

  RED("Project RED"),
  BLUE("Project BLUE"),
  GREEN("Project GREEN");

  private String name;

  ProjectName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.getName();
  }
}
