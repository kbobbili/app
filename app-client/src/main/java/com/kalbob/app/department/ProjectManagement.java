package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
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
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "department_project")
public class ProjectManagement extends BaseEntity {

  @EmbeddedId
  private ProjectManagementKey id;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("department_id")
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("project_id")
  @JoinColumn(name = "project_id")
  private Project project;

  private int rating;

}
