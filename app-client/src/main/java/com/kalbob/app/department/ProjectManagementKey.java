package com.kalbob.app.department;

import com.kalbob.app.project.Project;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Embeddable
public class ProjectManagementKey implements Serializable {

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("department_id")
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @MapsId("department_id")
  @JoinColumn(name = "project_id")
  private Project project;
}