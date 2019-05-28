package com.kalbob.app.task;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Accessors(chain = true)
@ToString(exclude = {"project"})
@EqualsAndHashCode(exclude = {"project"}, callSuper = true)
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  public Task setProject(Project project){
    if(project != null) project.getTasks().add(this);
    this.project = project;
    return this;
  }

  public Task removeProject() {
    if(project != null) {
      this.project.removeTask(this);
      this.project = null;
    }
    return this;
  }

  public Task setEmployee(Employee employee){
    if(employee != null) employee.getTasks().add(this);
    this.employee = employee;
    return this;
  }

  public Task removeEmployee() {
    if(employee != null) {
      this.employee.removeTask(this);
      this.employee = null;
    }
    return this;
  }
  
  
}
