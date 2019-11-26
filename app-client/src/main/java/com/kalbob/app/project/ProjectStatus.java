package com.kalbob.code.project;

public enum ProjectStatus {

  NOT_STARTED("Not Started"),
  STARTED("Started"),
  IN_PROGRESS("In Progress"),
  COMPLETED("Completed"),
  DELIVERED("Delivered");

  private String status;

  ProjectStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return this.getStatus();
  }
}
