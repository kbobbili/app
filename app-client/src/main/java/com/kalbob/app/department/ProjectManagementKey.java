package com.kalbob.app.department;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectManagementKey implements Serializable {

  @Column(name = "department_id")
  private Long departmentId;
  @Column(name = "project_id")
  private Long projectId;
}