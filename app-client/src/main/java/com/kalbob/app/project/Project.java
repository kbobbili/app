package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.ProjectManagement;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.task.Task;
import java.time.LocalDateTime;
import java.util.HashSet;
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
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"managingDepartment","projectAssignments","tasks"})
@EqualsAndHashCode(exclude = {"managingDepartment","projectAssignments","tasks"}, callSuper = true)
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

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "id.project", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  @OneToOne(mappedBy = "id.project", fetch = FetchType.EAGER)
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

  public Project setEmployees(Set<Employee> employees) {
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

  public Project addEmployee(@NonNull Employee employee) {
    if(this.projectAssignments == null){
      this.projectAssignments = new HashSet<>();
    }
    if(employee.getProjectAssignments() == null){
      employee.setProjectAssignments(new HashSet<>());
    }
    ProjectAssignment projectAssignment = new ProjectAssignment()
        .setProject(this)
        .setEmployee(employee)
        .setJoinedDate(LocalDateTime.now())
        .setIsCurrent(true)
        ;
    employee.getProjectAssignments().add(projectAssignment);
    this.projectAssignments.add(projectAssignment);
    return this;
  }

  public Project removeEmployee(@NonNull Employee employee) {
    if(this.projectAssignments == null || employee.getProjectAssignments() == null){
      return this;
    }
    ProjectAssignment projectAssignment = employee.getProjectAssignments().stream()
        .filter(pa -> pa.getProject() == this && pa.getIsCurrent())
        .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
    this.projectAssignments.remove(projectAssignment);
    projectAssignment.setLeftDate(LocalDateTime.now());  //hard delete would have been setEmp(null) & setPro(null)
    projectAssignment.setIsCurrent(false);
    this.projectAssignments.add(projectAssignment);
    return this;
  }

  public Set<Employee> getEmployees() {
    return this.projectAssignments.stream()
        .map(ProjectAssignment::getEmployee)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  public Project setTasks(Set<Task> tasks) {
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

  public Project addTask(@NonNull Task task) {
    if(this.tasks == null){
      this.tasks = new HashSet<>();
    }
    task.setProject(this);
    this.tasks.add(task);
    return this;
  }

  public Project removeTask(@NonNull Task task) {
    if (this.tasks == null) {
      return this;
    }
    Project project = task.getProject();
    project.getTasks().stream().filter(t -> t.getProject() == this)
        .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
    this.tasks.remove(task);
    task.setProject(null);
    return this;
  }

  public Project removeTasks() {
    if (this.tasks == null) {
      return this;
    }
    this.tasks.forEach(e -> e.setProject(null));
    this.tasks = null;
    return this;
  }

  public Set<Task> getTasks() {
    return this.tasks;
  }
}
