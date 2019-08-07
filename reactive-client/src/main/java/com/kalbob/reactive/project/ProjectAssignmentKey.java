package com.kalbob.reactive.project;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectAssignmentKey implements Serializable {

  @JoinColumn(name = "project_id")
  private Long projectId;
  @JoinColumn(name = "employee_id")
  private Long employeeId;
}