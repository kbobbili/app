package com.kalbob.app.es;

public enum SessionType {
  TWO_WAY("TWO_WAY"),
  ONE_WAY("ONE_WAY");

  private String type;

  SessionType(String type) {
    this.type = type;
  }

  public String type() {
    return type;
  }
}