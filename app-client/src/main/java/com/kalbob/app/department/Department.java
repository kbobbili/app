package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.employee.Employee;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@EqualsAndHashCode(exclude = {"address", "employees"}, callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department extends BaseEntity {

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "departmentHeaded", fetch = FetchType.EAGER)
  public Employee departmentHead;
  @Enumerated(EnumType.STRING)
  private DepartmentType type;
  private LocalDate startDate;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "department", fetch = FetchType.EAGER)
  private Address address;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>();

  public Department setAddress(Address address) {
    if(address != null) {
      address.setDepartment(this);
    }else{
      if(this.address != null){
        this.address.setDepartment(null);
      }
    }
    this.address = address;
    return this;
  }

  public Department setAddressUnidirectional(Address address) {
    this.address = address;
    return this;
  }

  public void removeAddress() {
    if(this.address != null){
      this.address.setDepartment(null);
    }
    this.address = null;
  }

  public Department setEmployees(List<Employee> employees) {
    if (employees != null) {
      employees.forEach(e -> e.setDepartment(this));
      this.employees = new HashSet<>(employees);
    }else{
      if (this.employees != null) {
        this.employees.forEach(e -> e.setDepartment(null));
        this.employees = null;
      }
    }
    return this;
  }

  public Department addEmployee(Employee employee) {
    if (this.employees != null && employee != null) {
      employee.setDepartment(this);
      this.employees.add(employee);
    }
    return this;
  }

  public Department removeEmployee(Employee employee) {
    if (this.employees != null && employee != null) {
      this.employees.remove(employee);
      employee.setDepartment(null);
    }
    return this;
  }

  public Department removeEmployees() {
    if (this.employees != null) {
      this.employees.forEach(e -> e.setDepartment(null));
      this.employees = null;
    }
    return this;
  }

  public List<Employee> getEmployees() {
    if (this.employees != null) {
      return new ArrayList<>(this.employees);
    } else {
      return new ArrayList<>();
    }
  }

  public Department setDepartmentHead(Employee employee) {
    if(employee != null) {
      employee.setDepartment(this);
    }else{
      if(this.departmentHead != null){
        this.departmentHead.setDepartment(null);
      }
    }
    this.departmentHead = employee;
    return this;
  }

  public void removeDepartmentHead() {
    if(this.departmentHead != null){
      this.departmentHead.setDepartment(null);
    }
    this.departmentHead = null;
  }

}
