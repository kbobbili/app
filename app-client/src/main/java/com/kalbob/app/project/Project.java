package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.ProjectManagement;
import com.kalbob.app.task.Task;
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
@ToString(exclude = {"projectManagement","projectAssignments","tasks"})
@EqualsAndHashCode(exclude = {"projectManagement","projectAssignments","tasks"}, callSuper = true)
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

  @OneToOne(mappedBy = "project", fetch = FetchType.LAZY)
  private ProjectManagement projectManagement;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
      name = "project_tasks",
      joinColumns = @JoinColumn(
          name = "project_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(
          name = "task_id", referencedColumnName = "id")
  )
  private Set<Task> tasks = new HashSet<>();

  public Set<ProjectAssignment> getProjectAssignments() {
    return this.projectAssignments;
  }

  public Project setProjectManagement(ProjectManagement projectManagement){
    this.projectManagement = projectManagement;
    if(projectManagement != null) projectManagement.setProject_Internal(this);
    return this;
  }

  public Project removeProjectManagement() {
    if(this.projectManagement != null) {
      this.projectManagement.setDepartment(null);
      this.projectManagement = null;
    }
    return this;
  }

  /*public Project setDepartment(@NonNull Department department){
    if(department != null) department.addProject(this);
    if(this.projectManagement != null) {
      this.projectManagement.setDepartment(department);
    }
    return this;
  }

  public Project removeDepartment() {
    if(this.projectManagement != null) {
      this.projectManagement.getDepartment().removeProject(this);
      this.projectManagement = null;
    }
    return this;
  }*/

  public Project setTasks(List<Task> tasks) {
    if (tasks != null) {
      this.tasks = new HashSet<>(tasks);
      tasks.forEach(e -> e.setProject(this));
    }else{
      removeTasks();
    }
    return this;
  }

  public Project addTask(@NonNull Task task) {
    if (this.tasks == null) {
      this.tasks = new HashSet<>();
    }
    if(!this.tasks.contains(task)){
      this.tasks.add(task);
      task.setProject(this);
    }
    return this;
  }

  public Project removeTask(@NonNull Task task) {
    if (this.tasks != null) {
      if (!this.tasks.contains(task))
        throw new ResourceNotFoundException();
      this.tasks.remove(task);
      task.setProject(null);
    }
    return this;
  }

  public Project removeTasks() {
    if (this.tasks != null) {
      this.tasks.forEach(e->e.setProject(null));
      this.tasks = null;
    }
    return this;
  }

  public List<Task> getTasks() {
    if(this.tasks != null) {
      return new ArrayList<>(this.tasks);
    }else{
      return new ArrayList<>();
    }
  }

  public Project setProjectAssignments(Set<ProjectAssignment> projectAssignments) {
    if (projectAssignments != null) {
      this.projectAssignments = projectAssignments;
      projectAssignments.forEach(e -> e.setProject(this));
    }else{
      removeProjectAssignments();
    }
    return this;
  }

  public Project addProjectAssignment(ProjectAssignment projectAssignment) {
    if (this.projectAssignments == null) {
      this.projectAssignments = new HashSet<>();
    }
    if(!this.projectAssignments.contains(projectAssignment)){
      this.projectAssignments.add(projectAssignment);
      projectAssignment.setProject(this);
    }
    return this;
  }

  public Project removeProjectAssignment(ProjectAssignment projectAssignment) {
    if (this.projectAssignments != null) {
      if (!this.projectAssignments.contains(projectAssignment))
        return this;
      projectAssignment.removeProject();
      this.projectAssignments.remove(projectAssignment);
      /*projectAssignment.setLeftDate(LocalDateTime.now());//meta-data or projectAssignment.setProject(null);
      projectAssignment.setIsCurrent(false);
      this.projectAssignments.add(projectAssignment);
      projectAssignment.addProject();*/
    }
    return this;
  }

  public Project removeProjectAssignments() {
    if (this.projectAssignments != null) {
      this.projectAssignments.forEach(e->e.setProject(null));
      this.projectAssignments = null;
    }
    return this;
  }


}
