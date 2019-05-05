package com.kalbob.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"employees"})
@Builder
@Entity
@Table(name = "project")
public class Project extends BaseModel{
    private String name;
    @ManyToMany(mappedBy = "projects",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @Fetch(FetchMode.JOIN)
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee){
        this.employees.add(employee);
        employee.getProjects().add(this);
    }

    public void removeEmployee(Employee employee){
        this.employees.remove(employee);
        employee.getProjects().remove(this);
    }
}
