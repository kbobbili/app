package com.kalbob.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee extends BaseModel{
    private String firstName;
    private String lastName;
    private Double salary;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "project_id", referencedColumnName = "id")
    )
    @Fetch(FetchMode.JOIN)
    @Builder.Default
    private Set<Project> projects = new HashSet<>();

    public void setProjects(List<Project> projects){
        if(projects !=null) this.projects = new HashSet<>(projects);
    }

    public List<Project> getProjects(){
        if(this.projects !=null) return new ArrayList<>(this.projects);
        else return new ArrayList<>();
    }

    public void addProject(Project project){
        if(this.projects != null) this.projects.add(project);
        if(project.getEmployees() != null ) project.getEmployees().add(this);
    }

    public void removeProject(Project project){
        if(this.projects != null) this.projects.remove(project);
        if(project.getEmployees() != null ) project.getEmployees().remove(this);
    }

    public void removeAllProjects(){
        this.projects = null;
    }

}
