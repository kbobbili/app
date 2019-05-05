package com.kalbob.app.data.model;

import java.util.Arrays;

public class ProjectMother {

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
}
