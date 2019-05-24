package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.employee.Employee;
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
import javax.persistence.OneToMany;
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
@ToString(exclude = {"projectAssignments"})
@EqualsAndHashCode(exclude = {"projectAssignments"}, callSuper = true)
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

  //private Boolean isCompleted; in resource based on status.

  @Setter(AccessLevel.NONE)
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "project", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  public List<ProjectAssignment> getProjectAssignments() {
    if (this.projectAssignments != null) {
      return new ArrayList<>(this.projectAssignments);
    } else {
      return new ArrayList<>();
    }
  }

  public Project setProjectAssignments(List<ProjectAssignment> projectAssignments) {
    if (projectAssignments != null) {
      projectAssignments.forEach(p -> p.setProject(this));
      this.projectAssignments = new HashSet<>(projectAssignments);
    }
    return this;
  }

  public List<Employee> getEmployees() {
    if (this.projectAssignments != null) {
      return this.projectAssignments.stream()
          .map(ProjectAssignment::getEmployee)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  public void addEmployees(List<Employee> employees){
    employees.forEach(this::addEmployee);
  }

  public void addEmployee(Employee employee) {
    ProjectAssignment projectAssignment = new ProjectAssignment()
        .setEmployee(employee)
        .setProject(this)
        .setJoinedDate(LocalDateTime.now())
        .setIsCurrent(true)
        ;
    if (this.projectAssignments != null) {
      this.projectAssignments.add(projectAssignment);
      if (employee.getProjectAssignments() != null) {
        employee.getProjectAssignments().add(projectAssignment);
      }
    }
  }

  public void removeEmployees(List<Employee> employees){
    employees.forEach(this::removeEmployee);
  }

  public void removeEmployee(Employee employee) {
    if (this.projectAssignments != null) {
      Optional<ProjectAssignment> projectAssignment = this.projectAssignments.stream()
          .filter(pa -> pa.getEmployee() == employee && pa.getIsCurrent())
          .findAny();
      projectAssignment.ifPresent(this::leaveProjectAssignment);//removeEmployee() means leaving the project. (not deleting).
    }
  }

  private void leaveProjectAssignment(ProjectAssignment pa) {//it is leave(), so just refresh the pa. (it is not remove(), so no set(null))
    this.projectAssignments.remove(pa);
    pa.setLeftDate(LocalDateTime.now());
    pa.setIsCurrent(false);
    this.projectAssignments.add(pa);
  }

  public void removeProjectAssignments() {
    this.projectAssignments = null;
  }
}
