package com.kalbob.app.data.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"address", "employees"})
@EqualsAndHashCode(exclude = {"address", "employees"})
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department extends BaseModel {

  private String name;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "department", fetch = FetchType.EAGER)
  private Address address;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>();

  public Department setAddress(Address address) {
    this.address = address;
    return this;
  }

  public void removeAddress() {
    this.address = null;
  }

  public List<Employee> getEmployees() {
    if (this.employees != null) {
      return new ArrayList<>(this.employees);
    } else {
      return new ArrayList<>();
    }
  }

  public Department setEmployees(List<Employee> employees) {
    if (employees != null) {
      employees.stream().forEach(e -> e.setDepartment(this));
      this.employees = new HashSet<>(employees);
    }
    return this;
  }

  public void addEmployee(Employee employee) {
    if (this.employees != null) {
      this.employees.add(employee);
    }
    if (employee != null) {
      employee.setDepartment(this);
    }
  }

  public void removeEmployee(Employee employee) {
    if (this.employees != null) {
      this.employees.remove(employee);
    }
    if (employee != null) {
      employee.setDepartment(null);
    }
  }

  public void removeEmployees() {
    this.employees = null;
  }

}
