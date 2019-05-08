package com.kalbob.app.data.model;

public class AddressMother extends ObjectMother {

  public static Address simple() {
    return new Address()
        .setAptNum("150")
        .setStreet("N Tatum")
        .setCity("Scottsdale")
        .setState("Arizona")
        .setZipCode("85254")
        ;
  }

  public static Address complete() {
    return simple()
        .setDepartment(DepartmentMother.simple())
        ;
  }

  public static Address simpleRandom() {
    return new Address()
        .setAptNum(faker.number().digits(3))
        .setStreet(faker.address().streetName())
        .setCity(faker.address().city())
        .setStreet(faker.address().state())
        .setZipCode(faker.address().zipCode())
        ;
  }

  public static Address completeRandom() {
    return simpleRandom()
        .setDepartment(DepartmentMother.simpleRandom())
        ;
  }

}
