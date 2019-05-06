package com.kalbob.app.data.model;

public class AddressMother extends ObjectMother {

  public static Address.AddressBuilder simple() {
    return Address.builder()
        .aptNum("150")
        .street("N Tatum")
        .city("Scottsdale")
        .state("Arizona")
        .zipCode("85254")
        ;
  }

  public static Address.AddressBuilder complete() {
    return simple()
        .department(DepartmentMother.simple().build())
        ;
  }

  public static Address.AddressBuilder simpleRandom() {
    return Address.builder()
        .aptNum(faker.number().digits(3))
        .street(faker.address().streetName())
        .city(faker.address().city())
        .state(faker.address().state())
        .zipCode(faker.address().zipCode())
        ;
  }

  public static Address.AddressBuilder completeRandom() {
    return simpleRandom()
        .department(DepartmentMother.simpleRandom().build())
        ;
  }

}
