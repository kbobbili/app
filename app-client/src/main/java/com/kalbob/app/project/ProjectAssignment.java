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
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
      this.project.getProjectAssignments().remove(this);//actual object set to null
      //this.project.removeProjectAssignment(this);//meta-data
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
      this.employee.getProjectAssignments().remove(this);//actual object set to null
      //this.employee.removeProjectAssignment(this);//meta-data
      this.employee = null;
    }
    return this;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ProjectAssignment)) {
      return false;
    }
    final ProjectAssignment other = (ProjectAssignment) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    final Object this$project = this.getProject();
    final Object other$project = other.getProject();
    if (this$project == null ? other$project != null : !this$project.equals(other$project)) {
      return false;
    }
    final Object this$employee = this.getEmployee();
    final Object other$employee = other.getEmployee();
    if (this$employee == null ? other$employee != null : !this$employee.equals(other$employee)) {
      return false;
    }
    final Object this$joinedDate = this.getJoinedDate();
    final Object other$joinedDate = other.getJoinedDate();
    if (this$joinedDate == null ? other$joinedDate != null
        : !this$joinedDate.equals(other$joinedDate)) {
      return false;
    }
    final Object this$leftDate = this.getLeftDate();
    final Object other$leftDate = other.getLeftDate();
    if (this$leftDate == null ? other$leftDate != null : !this$leftDate.equals(other$leftDate)) {
      return false;
    }
    final Object this$isCurrent = this.getIsCurrent();
    final Object other$isCurrent = other.getIsCurrent();
    if (this$isCurrent == null ? other$isCurrent != null
        : !this$isCurrent.equals(other$isCurrent)) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof ProjectAssignment;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $joinedDate = this.getJoinedDate();
    result = result * PRIME + ($joinedDate == null ? 43 : $joinedDate.hashCode());
    final Object $leftDate = this.getLeftDate();
    result = result * PRIME + ($leftDate == null ? 43 : $leftDate.hashCode());
    final Object $isCurrent = this.getIsCurrent();
    result = result * PRIME + ($isCurrent == null ? 43 : $isCurrent.hashCode());
    return result;
  }
}
