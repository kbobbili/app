package com.kalbob.app.employee;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.Department;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(exclude = {"manager", "employees", "projectAssignments"})
@EqualsAndHashCode(exclude = {"manager", "employees", "projectAssignments"}, callSuper = true)
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

  @OneToOne
  @JoinColumn
  public Department departmentHeaded;

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  private JobType jobType;

  private Double salary;

  private Double commission;

  private LocalDate hireDate;

  private LocalDate relievingDate;

  @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id")
  @Setter(AccessLevel.NONE)
  private Department department;

  //@Setter(AccessLevel.NONE)
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  @ManyToOne(cascade = {CascadeType.PERSIST})
  @JoinColumn(name = "manager_id")
  @Setter(AccessLevel.NONE)
  private Employee manager;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "manager", fetch = FetchType.LAZY)
  @Setter(AccessLevel.NONE)
  private Set<Employee> employees = new HashSet<>();

  public Employee setDepartment(Department department) {
    this.department = department;
    return this;
  }

  public void removeDepartment() {
    this.department = null;
  }

  public List<ProjectAssignment> getProjectAssignments() {
    if (this.projectAssignments != null) {
      return new ArrayList<>(this.projectAssignments);
    } else {
      return new ArrayList<>();
    }
  }

  public Employee setProjectAssignments(List<ProjectAssignment> projectAssignments) {
    if (projectAssignments != null) {
      projectAssignments.forEach(p -> p.setEmployee(this));
      this.projectAssignments = new HashSet<>(projectAssignments);
    }
    return this;
  }

  public List<Project> getProjects() {
    if (this.projectAssignments != null) {
      return this.projectAssignments.stream()
          .map(ProjectAssignment::getProject)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  public void joinProject(Project project) {
    ProjectAssignment projectAssignment = new ProjectAssignment()
        .setEmployee(this)
        .setProject(project)
        .setJoinedDate(LocalDateTime.now())
        .setIsCurrent(true)
        ;
    if (this.projectAssignments != null) {
      this.projectAssignments.add(projectAssignment);
      if (project.getProjectAssignments() != null) {
        project.getProjectAssignments().add(projectAssignment);
      }
    }
  }

  public void leaveProject(Project project) {
    if (this.projectAssignments != null) {
      Optional<ProjectAssignment> projectAssignment = this.projectAssignments.stream()
          .filter(pa -> pa.getProject() == project && pa.getIsCurrent())
          .findAny();
      projectAssignment.ifPresent(this::leaveProjectAssignment);
    }
  }

  public void removeProjectAssignments() {
    this.projectAssignments = null;
  }

  private void leaveProjectAssignment(ProjectAssignment pa) {
    this.projectAssignments.remove(pa);
    pa.setLeftDate(LocalDateTime.now());
    pa.setIsCurrent(false);
    this.projectAssignments.add(pa);
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
      employees.forEach(e -> e.setManager(this));
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
