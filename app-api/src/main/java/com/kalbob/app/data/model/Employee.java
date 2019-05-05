package com.kalbob.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee extends BaseModel{
    private String name;
    private Double salary;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "project_id", referencedColumnName = "id")
    )
    @Fetch(FetchMode.JOIN)
    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    public void removeProject(Project project){
        this.projects.remove(project);
        project.getEmployees().remove(Employee.this);
    }

    public void removeAllProjects(){
        this.projects = null;
    }

}
