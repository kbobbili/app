package com.kalbob.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"employees"})
@EqualsAndHashCode(exclude = {"employees"})
@Builder
@Entity
@Table(name = "project")
public class Project extends BaseModel{
    private String name;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "projects", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();

    public void setEmployees(List<Employee> employees){
        if(employees !=null) this.employees = new HashSet<>(employees);
    }

    public List<Employee> getEmployees(){
        if(this.employees !=null) return new ArrayList<>(this.employees);
        else return new ArrayList<>();
    }

    public void addEmployee(Employee employee){
        if(this.employees != null) this.employees.add(employee);
        if(employee.getProjects() != null ) employee.getProjects().add(this);
    }

    public void removeEmployee(Employee employee){
        if(this.employees != null) this.employees.remove(employee);
        if(employee.getProjects() != null ) employee.getProjects().remove(this);
    }

    public void removeAllEmployees(){
        this.employees = null;
    }
}
