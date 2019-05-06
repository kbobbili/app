package com.kalbob.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"address", "employees"})
@Builder
@Entity
@Table(name = "department")
public class Department extends BaseModel{
    private String name;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "department")
    private Address address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();

    public void assignAddress(Address address){
        this.address = address;
        if(address != null) address.setDepartment(this);
    }

    public void removeAddress(){
        if(this.address != null) this.address.setDepartment(null);
        this.address = null;
    }

    public void addEmployee(Employee employee){
        if(this.employees != null) this.employees.add(employee);
        if(employee != null) employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee){
        if(this.employees != null) this.employees.remove(employee);
        if(employee != null) employee.setDepartment(null);
    }

}
