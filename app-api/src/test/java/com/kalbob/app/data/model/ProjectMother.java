package com.kalbob.app.data.model;

import java.util.Arrays;

public class ProjectMother extends ObjectMother{

    public static Project.ProjectBuilder simple(){
        return Project.builder()
                .name("Project RED")
                ;
    }

    public static Project.ProjectBuilder complete(){
        return simple()
                .employees(Arrays.asList(EmployeeMother.simple().build()))
                ;
    }

    public static Project.ProjectBuilder simpleRandom(){
        return Project.builder()
                .name(dataFactory.getItem(Arrays.asList("Project RED", "Project Blue", "Project Green")))
                ;
    }

    public static Project.ProjectBuilder completeRandom(){
        return simpleRandom()
                .employees(Arrays.asList(EmployeeMother.simpleRandom().build()))
                ;
    }
}
