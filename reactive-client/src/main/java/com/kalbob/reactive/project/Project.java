package com.kalbob.reactive.project;

import com.kalbob.reactive.BaseEntity;
import com.kalbob.reactive.employee.Employee;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
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
@ToString(exclude = {"employees"})
@EqualsAndHashCode(exclude = {"employees"}, callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private ProjectName name;
  private LocalDateTime startDate;
  private LocalDateTime estimatedEndDate;
  @Enumerated(EnumType.STRING)
  private ProjectStatus status;
  private LocalDateTime endDate;
  private Boolean isCompleted;
  @ManyToMany(cascade = {CascadeType.PERSIST,
      CascadeType.MERGE}, mappedBy = "projects", fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>();

  public List<Employee> getEmployees() {
    if (this.employees != null) {
      return new ArrayList<>(this.employees);
    } else {
      return new ArrayList<>();
    }
  }

  public Project setEmployees(List<Employee> employees) {
    if (employees != null) {
      employees.forEach(e -> e.addProject(this));
      this.employees = new HashSet<>(employees);
    }
    return this;
  }

  public void addEmployee(Employee employee) {
    if (this.employees != null) {
      this.employees.add(employee);
    }
    if (employee.getProjects() != null) {
      employee.getProjects().add(this);
    }
  }

  public void removeEmployee(Employee employee) {
    if (this.employees != null) {
      this.employees.remove(employee);
    }
    if (employee.getProjects() != null) {
      employee.getProjects().remove(this);
    }
  }

  public void removeEmployees() {
    this.employees = null;
  }
}
