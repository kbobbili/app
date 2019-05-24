package com.kalbob.app.project;

import com.kalbob.app.BaseEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"projectAssignments"})
@EqualsAndHashCode(exclude = {"projectAssignments"}, callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private ProjectName name;

  private LocalDateTime startDate;

  private LocalDateTime estimatedEndDate;

  @Enumerated(EnumType.STRING)
  private ProjectStatus status;

  private LocalDateTime endDate;

  private Boolean isCompleted; //redundant. may be put in dto.

  @Setter(AccessLevel.NONE)
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "project", fetch = FetchType.LAZY)
  private Set<ProjectAssignment> projectAssignments = new HashSet<>();

  public List<ProjectAssignment> getProjectAssignments() {
    if (this.projectAssignments != null) {
      return new ArrayList<>(this.projectAssignments);
    } else {
      return new ArrayList<>();
    }
  }

  public Project setProjectAssignments(List<ProjectAssignment> projectAssignments) {
    if (projectAssignments != null) {
      projectAssignments.forEach(p -> p.setProject(this));
      this.projectAssignments = new HashSet<>(projectAssignments);
    }
    return this;
  }

  public void removeProjectAssignments() {
    this.projectAssignments = null;
  }
}
