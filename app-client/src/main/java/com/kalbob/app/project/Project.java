package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.employee.Employee;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

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

  private Boolean isCompleted;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "project", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  public Project setEmployees(List<Employee> employees) {
    if (employees != null) {
      this.projectAssignments = employees.stream().map(e -> new ProjectAssignment()
          .setProject(this)
          .setEmployee(e)
          .setJoinedDate(LocalDateTime.now())
          .setIsCurrent(true)
      ).collect(Collectors.toSet());
    }else{
      if(this.projectAssignments != null) {
        this.projectAssignments.forEach(pa -> {
          pa
              .setProject(null)
              .setEmployee(null);
        });
        this.projectAssignments = null;
      }
    }
    return this;
  }

  public Project addEmployee(Employee employee) {
    ProjectAssignment projectAssignment = new ProjectAssignment()
        .setProject(this)
        .setEmployee(employee)
        .setJoinedDate(LocalDateTime.now())
        .setIsCurrent(true)
        ;
    if (this.projectAssignments != null) {
      this.projectAssignments.add(projectAssignment);
      if (employee.getProjectAssignments() != null) {
        employee.getProjectAssignments().add(projectAssignment);
      }
    }
    return this;
  }

  public Project removeEmployee(Employee employee) {
    if (this.projectAssignments != null) {
      ProjectAssignment projectAssignment = employee.getProjectAssignments().stream()
          .filter(pa -> pa.getProject() == this && pa.getIsCurrent())
          .findAny().orElseThrow(ResourceNotFoundException::new);
      this.projectAssignments.remove(projectAssignment);
      projectAssignment.setLeftDate(LocalDateTime.now());
      projectAssignment.setIsCurrent(false);
      this.projectAssignments.add(projectAssignment);
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
}
