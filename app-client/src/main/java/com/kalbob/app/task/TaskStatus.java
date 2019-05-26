package com.kalbob.app.task;

public enum TaskStatus {

  NOT_STARTED("Not Started"),
  STARTED("Started"),
  IN_PROGRESS("In Progress"),
  COMPLETED("Completed"),
  DELIVERED("Delivered");

  private String status;

  TaskStatus(String status) {
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
