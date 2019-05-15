package com.kalbob.app.department;

public enum DepartmentType {

  FINANCE("Finance"),
  SALES("Sales"),
  HR("Human Resources"),
  IT("Information Technology"),
  MARKETING("Marketing"),
  ADVERTISING("Advertising");

  private String type;

  DepartmentType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return this.getType();
  }
}
