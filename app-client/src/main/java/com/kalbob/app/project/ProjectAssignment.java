package com.kalbob.code.project;

import com.kalbob.code.employee.Employee;
import java.time.LocalDateTime;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
@ToString(exclude = {"employee"})
@EqualsAndHashCode(exclude = {"employee"})
@Accessors(chain = true)
@Entity
@Table(name = "project_assignment")
public class ProjectAssignment {

  @EmbeddedId
  ProjectAssignmentKey id;
  @ManyToOne
  @MapsId("project_id")
  @JoinColumn(name = "id")
  private Project project;
  @ManyToOne
  @MapsId("employee_id")
  @JoinColumn(name = "id")
  private Employee employee;
  private LocalDateTime assignmentDate;
}
