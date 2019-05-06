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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"address", "employees"})
@EqualsAndHashCode(exclude = {"address", "employees"})
@Builder
@Entity
@Table(name = "department")
public class Department extends BaseModel {

  private String name;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "department", fetch = FetchType.EAGER)
  private Address address;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Employee> employees = new HashSet<>();

  public void setAddress(Address address) {
    this.address = address;
    if (address != null) {
      address.setDepartment(this);
    }
  }

  public void removeAddress() {
    if (this.address != null) {
      this.address.setDepartment(null);
    }
    this.address = null;
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

  public List<Employee> getEmployees() {
    if (this.employees != null) {
      return new ArrayList<>(this.employees);
    } else {
      return new ArrayList<>();
    }
  }

  public void setEmployees(List<Employee> employees) {
    if (employees != null) {
      this.employees = new HashSet<>(employees);
    }
  }

}
