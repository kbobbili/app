package com.kalbob.app.data.model;

import java.util.Arrays;

public class DepartmentMother extends ObjectMother{

    public static Department.DepartmentBuilder simple(){
        return Department.builder()
                .name("Finance")
                ;
    }

    public static Department.DepartmentBuilder simpleRandom(){
        return Department.builder()
                .name(dataFactory.getItem(Arrays.asList("Finance","Sales","Business","Marketing","IT")))
                ;
    }

}
