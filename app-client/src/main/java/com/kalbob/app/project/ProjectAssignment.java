package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.employee.Employee;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "project_assignment")
public class ProjectAssignment extends BaseEntity {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "project_id")
  private Project project;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "employee_id")
  private Employee employee;
  private LocalDateTime joinedDate;
  private LocalDateTime leftDate;
  private Boolean isCurrent;

  public ProjectAssignment setProject(Project project){
    if(project != null) project.getProjectAssignments().add(this);
    this.project = project;
    return this;
  }

  public ProjectAssignment setProject_Internal(Project project){
    this.project = project;
    return this;
  }

  public ProjectAssignment removeProject() {
    if(project != null) {
      this.project.removeProjectAssignment(this);
      this.project = null;
    }
    return this;
  }

  public ProjectAssignment setEmployee(Employee employee){
    if(employee != null) employee.getProjectAssignments().add(this);
    this.employee = employee;
    return this;
  }

  public ProjectAssignment setEmployee_Internal(Employee employee){
    this.employee = employee;
    return this;
  }

  public ProjectAssignment removeEmployee() {
    if(employee != null) {
      this.employee.removeProjectAssignment(this);
      this.employee = null;
    }
    return this;
  }
  
  
}
