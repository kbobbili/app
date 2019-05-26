package com.kalbob.app.employee;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.Department;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.task.Task;
import java.time.LocalDate;
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
import javax.persistence.ManyToOne;
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
@Accessors(chain = true)
@ToString(exclude = {"manager", "reportees"})
@EqualsAndHashCode(exclude = {"manager", "reportees"}, callSuper = true)
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

  private String firstName;

  private String lastName;

  @Enumerated(EnumType.STRING)
  private JobType jobType;

  private Double salary;

  private Double commission;

  private LocalDate hireDate;

  private LocalDate relievingDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "manager_id")
  private Employee manager;

  @OneToOne(mappedBy = "departmentHead")
  public Department departmentHeaded;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "manager", fetch = FetchType.LAZY)
  private Set<Employee> reportees = new HashSet<>();

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<Task> tasks = new HashSet<>();

  public Employee setDepartment(Department department) {
    if(department != null) {
      if(department.getEmployees() != null) department.getEmployees().add(this);//Todo
    }else{
      if(this.department != null){
        this.department.getEmployees().remove(this);
      }
    }
    this.department = department;
    return this;
  }

  public Employee removeDepartment() {
    if(this.department != null){
      this.department.getEmployees().remove(this);
      this.department = null;
    }
    return this;
  }

  public Employee setManager(Employee manager) {
    if(manager != null) {
      if(manager.getReportees() != null) manager.getReportees().add(this);//Todo
    }else{
      if(this.manager != null){
        this.manager.getReportees().remove(this);
      }
    }
    this.manager = manager;
    return this;
  }

  public Employee removeManager() {
    if(this.manager != null){
      this.manager.getReportees().remove(this);
      this.manager = null;
    }
    return this;
  }

  public List<Employee> getReportees() {
    if (this.reportees != null) {
      return new ArrayList<>(this.reportees);
    } else {
      return new ArrayList<>();
    }
  }

  public Employee setReportees(List<Employee> reportees) {
    if (reportees != null) {
      reportees.forEach(e -> e.setManager(this));
      this.reportees = new HashSet<>(reportees);
    }else{
      if(this.reportees != null) {
        this.reportees.forEach(e -> e.setManager(null));
        this.reportees = null;
      }
    }
    return this;
  }

  public Employee addReportee(Employee reportee) {
    if (this.reportees != null && reportee != null) {
      reportee.setManager(this);
      this.reportees.add(reportee);
    }
    return this;
  }

  public Employee removeReportee(Employee reportee) {
    if (this.reportees != null && reportee != null) {
      this.reportees.remove(reportee);
      reportee.setManager(null);
    }
    return this;
  }

  public Employee removeReportees() {
    if (this.reportees != null) {
      this.reportees.forEach(e -> e.setManager(null));
      this.reportees = null;
    }
    return this;
  }

  public Employee setProjects(List<Project> projects) {
    if (projects != null) {
      this.projectAssignments = projects.stream().map(p -> new ProjectAssignment()
          .setProject(p)
          .setEmployee(this)
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

  public Employee joinProject(Project project) {
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
      }//Todo
    }
    return this;
  }

  public Employee leaveProject(Project project) {
    if (this.projectAssignments != null) {
      ProjectAssignment projectAssignment = project.getProjectAssignments().stream()
          .filter(pa -> pa.getEmployee() == this && pa.getIsCurrent())
          .findAny().orElseThrow(ResourceNotFoundException::new);
      this.projectAssignments.remove(projectAssignment);
      projectAssignment.setLeftDate(LocalDateTime.now());
      projectAssignment.setIsCurrent(false);
      this.projectAssignments.add(projectAssignment);
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

  public Employee setDepartmentHeaded(Department department) {
    if(department != null) {
      department.setDepartmentHeadUnidirectional(this);
    }else{
      if(this.departmentHeaded != null){
        this.departmentHeaded.setDepartmentHeadUnidirectional(null);
      }
    }
    this.departmentHeaded = department;
    return this;
  }

  public void removeDepartmentHeaded() {
    if(this.departmentHeaded != null){
      this.departmentHeaded.setDepartmentHead(null);
    }
    this.departmentHeaded = null;
  }

  public Employee setTasks(List<Task> tasks) {
    if (tasks != null) {
      tasks.forEach(e -> e.setEmployee(this));
      this.tasks = new HashSet<>(tasks);
    }else{
      if (this.tasks != null) {
        this.tasks.forEach(e -> e.setEmployee(null));
        this.tasks = null;
      }
    }
    return this;
  }

  public Employee addTask(Task task) {
    if (this.tasks != null && task != null) {
      task.setEmployee(this);
      this.tasks.add(task);
    }
    return this;
  }

  public Employee removeTask(Task task) {
    if (this.tasks != null && task != null) {
      this.tasks.remove(task);
      task.setEmployee(null);
    }
    return this;
  }

  public Employee removeTasks() {
    if (this.tasks != null) {
      this.tasks.forEach(e -> e.setEmployee(null));
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
