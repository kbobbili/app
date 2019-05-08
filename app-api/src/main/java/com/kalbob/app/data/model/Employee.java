package com.kalbob.app.data.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"manager", "employees"})
@Entity
@Table(name = "employee")
public class Employee extends BaseModel {

  private String firstName;
  private String lastName;
  @Enumerated(EnumType.STRING)
  private JobType jobType;
  private Double salary;
  private Double commission;
  private LocalDate joiningDate;
  @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id")
  @Setter(AccessLevel.NONE)
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
  @Setter(AccessLevel.NONE)
  private Set<Project> projects = new HashSet<>();
  @ManyToOne(cascade = {CascadeType.PERSIST})
  @JoinColumn(name = "manager_id")
  @Setter(AccessLevel.NONE)
  private Employee manager;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "manager", fetch = FetchType.LAZY)
  @Setter(AccessLevel.NONE)
  private Set<Employee> employees = new HashSet<>();
  @OneToOne
  @JoinColumn
  public Department departmentHeaded;

  public Employee setDepartment(Department department) {
    this.department = department;
    return this;
  }

  public void removeDepartment() {
    this.department = null;
  }

  public List<Project> getProjects() {
    if (this.projects != null) {
      return new ArrayList<>(this.projects);
    } else {
      return new ArrayList<>();
    }
  }

  public Employee setProjects(List<Project> projects) {
    if (projects != null) {
      projects.stream().forEach(p -> p.addEmployee(this));
      this.projects = new HashSet<>(projects);
    }
    return this;
  }

  public void addProject(Project project) {
    if (this.projects != null) {
      this.projects.add(project);
    }
    if (project.getEmployees() != null) {
      project.getEmployees().add(this);
    }
  }

  public void removeProject(Project project) {
    if (this.projects != null) {
      this.projects.remove(project);
    }
    if (project.getEmployees() != null) {
      project.getEmployees().remove(this);
    }
  }

  public void removeProjects() {
    this.projects = null;
  }

  public Employee setManager(Employee manager) {
    this.manager = manager;
    return this;
  }

  public void removeManager() {
    this.manager = null;
  }

  public List<Employee> getEmployees() {
    if (this.employees != null) {
      return new ArrayList<>(this.employees);
    } else {
      return new ArrayList<>();
    }
  }

  public Employee setEmployees(List<Employee> employees) {
    if (employees != null) {
      employees.stream().forEach(e -> e.setManager(this));
      this.employees = new HashSet<>(employees);
    }
    return this;
  }

  public void addEmployee(Employee employee) {
    if (this.employees != null) {
      this.employees.add(employee);
    }
    if (employee != null) {
      employee.setManager(this);
    }
  }

  public void removeEmployee(Employee employee) {
    if (this.employees != null) {
      this.employees.remove(employee);
    }
    if (employee != null) {
      employee.setManager(null);
    }
  }

  public void removeEmployees() {
    this.employees = null;
  }

}
