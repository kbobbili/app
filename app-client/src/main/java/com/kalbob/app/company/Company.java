package com.kalbob.app.company;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.Department;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
@Table(name = "company")
public class Company extends BaseEntity {

  private String name;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY)
  private Set<Department> departments = new HashSet<>();

  public Company setDepartments(List<Department> departments) {
    if (departments != null) {
      departments.forEach(e -> e.setCompany(this));
      this.departments = new HashSet<>(departments);
    }else{
      if (this.departments != null) {
        this.departments.forEach(e -> e.setCompany(null));
        this.departments = null;
      }
    }
    return this;
  }

  public Company addDepartment(Department department) {
    if (this.departments != null && department != null) {
      department.setCompany(this);
      this.departments.add(department);
    }
    return this;
  }

  public Company removeDepartment(Department department) {
    if (this.departments != null && department != null) {
      this.departments.remove(department);
      department.setCompany(null);
    }
    return this;
  }

  public Company removeDepartments() {
    if (this.departments != null) {
      this.departments.forEach(e -> e.setCompany(null));
      this.departments = null;
    }
    return this;
  }

  public List<Department> getDepartments() {
    if (this.departments != null) {
      return new ArrayList<>(this.departments);
    } else {
      return new ArrayList<>();
    }
  }
}
