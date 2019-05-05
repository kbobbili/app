package com.kalbob.app.data.model;

public class AddressMother {

    public static Address.AddressBuilder complete(){
        return Address.builder()
                .aptNum("150")
                .state("N Tatum")
                .city("Scottsdale")
                .state("Arizona")
                .zipCode("85254")
                ;
    }

}
