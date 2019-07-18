package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.project.Project;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "project_management")
public class ProjectManagement extends BaseEntity {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "project_id")
  private Project project;

  private LocalDateTime startDate;
  private LocalDateTime endDate;

  private int rating;

  public ProjectManagement setDepartment(Department department){
    this.department = department;
    if(department != null) department.getProjectManagements().add(this);
    return this;
  }

  public ProjectManagement setDepartment_Internal(Department department){
    this.department = department;
    return this;
  }

  public ProjectManagement removeDepartment() {
    if(department != null) {
      this.department.removeProjectManagement(this);
      this.department = null;
    }
    return this;
  }

  public ProjectManagement setProject(Project project){
    this.project = project;
    if(project != null) project.setProjectManagement(this);
    return this;
  }

  public ProjectManagement setProject_Internal(Project project){
    this.project = project;
    return this;
  }

  public ProjectManagement removeProject() {
    if(this.project != null) {
      this.project.setProjectManagement(null);
      this.project = null;
    }
    return this;
  }


}
