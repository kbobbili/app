package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.ProjectManagement;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.task.Task;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

  @OneToOne(mappedBy = "project", fetch = FetchType.EAGER)
  private ProjectManagement managingDepartment;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "project_tasks",
      joinColumns = @JoinColumn(
          name = "project_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "task_id", referencedColumnName = "id")
  )
  private Set<Task> tasks = new HashSet<>();

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

  public Project setTasks(List<Task> tasks) {
    if (tasks != null) {
      tasks.forEach(e -> e.setProject(this));
      this.tasks = new HashSet<>(tasks);
    }else{
      if (this.tasks != null) {
        this.tasks.forEach(e -> e.setProject(null));
        this.tasks = null;
      }
    }
    return this;
  }

  public Project addTask(Task task) {
    if (this.tasks != null && task != null) {
      task.setProject(this);
      this.tasks.add(task);
    }
    return this;
  }

  public Project removeTask(Task task) {
    if (this.tasks != null && task != null) {
      this.tasks.remove(task);
      task.setProject(null);
    }
    return this;
  }

  public Project removeTasks() {
    if (this.tasks != null) {
      this.tasks.forEach(e -> e.setProject(null));
      this.tasks = null;
    }
    return this;
  }

  public List<Task> getTasks() {
    if (this.tasks != null) {
      return new ArrayList<>(this.tasks);
    } else {
      return new ArrayList<>();
    }
  }
}
