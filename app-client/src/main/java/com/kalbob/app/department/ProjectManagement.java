package com.kalbob.app.department;

import com.kalbob.app.project.Project;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "project_management")
public class ProjectManagement {

  @EmbeddedId
  @EqualsAndHashCode.Include
  private ProjectManagementKey id;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("department_id")
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("project_id")
  @JoinColumn(name = "project_id")
  private Project project;

  private int rating;

  public ProjectManagement setDepartment(Department department) {
    if(this.department != department) {
      if(this.department != null) this.department.removeProjectManagement(this);
      this.department = department;
      if(department != null) department.addProjectManagement(this);
    }
    return this;
  }

  public ProjectManagement removeDepartment() {
    if(this.department != null){
      this.department.removeProjectManagement(this);
      this.department = null;
    }
    return this;
  }

  public ProjectManagement setProject(Project project) {
    if(this.project != project) {
      if(this.project != null) this.project.removeProjectManagement();
      this.project = project;
      if(project != null) project.setProjectManagement(this);
    }
    return this;
  }

  public ProjectManagement removeProject() {
    if(this.project != null){
      this.project.setProjectManagement(null);
      this.project = null;
    }
    return this;
  }
}
