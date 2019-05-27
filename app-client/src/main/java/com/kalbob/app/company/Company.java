package com.kalbob.app.company;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.department.Department;
import java.util.Collections;
import java.util.HashSet;
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
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(exclude = {"departments"})
@EqualsAndHashCode(exclude = {"departments"}, callSuper = true)
@Entity
@Table(name = "company")
public class Company extends BaseEntity {

  private String name;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY)
  private Set<Department> departments = new HashSet<>();

  public Company setDepartments(Set<Department> departments) {
    if (departments != null) {
      this.departments = new HashSet<>(departments);
      departments.forEach(e -> e.setCompany(this));
    }else{
      if (this.departments != null) {
        this.departments.forEach(Department::removeCompany);
        this.departments = null;
      }
    }
    return this;
  }

  public Company addDepartment(@NonNull Department department) {
    if (this.departments == null) {
      this.departments = new HashSet<>();
    }
    if(!this.departments.contains(department)){
      this.departments.add(department);
      department.setCompany(this);
    }
    return this;
  }

  public Company removeDepartment(@NonNull Department department) {
    if (this.departments == null) {
      return this;
    }
    if(this.departments.contains(department)) {
      Company company = department.getCompany();
      company.getDepartments().stream().filter(t -> t.getCompany() == this)
          .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
      this.departments.remove(department);
      department.setCompany(null);
    }
    return this;
  }

  public Company removeDepartments() {
    if (this.departments != null) {
      this.departments.forEach(Department::removeCompany);
      this.departments = null;
    }
    return this;
  }

  public Set<Department> getDepartments() {
    return Collections.unmodifiableSet(this.departments);
  }
}
