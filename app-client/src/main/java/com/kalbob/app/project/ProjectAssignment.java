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

  public ProjectAssignment setProject(Project project) {
    if(this.project != project) {
      if(this.project != null) this.project.removeProjectAssignment(this);
      this.project = project;
      if(project != null) project.addProjectAssignment(this);
    }
    return this;
  }

  public ProjectAssignment removeProject() {
    if(this.project != null){
      this.project.removeProjectAssignment(this);
      this.project = null;
    }
    return this;
  }

  public ProjectAssignment setEmployee(Employee employee) {
    if(this.employee != employee) {
      if(this.employee != null) this.employee.removeProjectAssignment(this);
      this.employee = employee;
      if(employee != null) employee.addProjectAssignment(this);
    }
    return this;
  }

  public ProjectAssignment removeEmployee() {
    if(this.employee != null){
      this.employee.removeProjectAssignment(this);
      this.employee = null;
    }
    return this;
  }
  
  
}
