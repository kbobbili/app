package com.kalbob.app.data.model;

import java.util.Arrays;
import java.util.HashSet;

public class ProjectMother extends ObjectMother{

    public static Project.ProjectBuilder simple(){
        return Project.builder()
                .name("Project RED")
                ;
    }

    public static Project.ProjectBuilder complete(){
        return simple()
                .employees(new HashSet<>(Arrays.asList(EmployeeMother.simple().build())))
                ;
    }

    public static Project.ProjectBuilder simpleRandom(){
        return Project.builder()
                .name(dataFactory.getItem(Arrays.asList("Project Blue", "Project Green")))
                ;
    }

    public static Project.ProjectBuilder completeRandom(){
        return simpleRandom()
                .employees(new HashSet<>(Arrays.asList(EmployeeMother.simpleRandom().build())))
                ;
    }
}
