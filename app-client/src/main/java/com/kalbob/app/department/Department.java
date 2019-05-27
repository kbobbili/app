package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@ToString(exclude = {"employees", "projectManagements"})
@EqualsAndHashCode(exclude = {"employees", "projectManagements"}, callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @Enumerated(EnumType.STRING)
  private DepartmentType type;

  private LocalDate startDate;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "department_head")
  public Employee departmentHead;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<ProjectManagement> projectManagements = new HashSet<>();

  public Department setAddress(Address address) {
    if(this.address != address){
      if(this.address != null) this.address.removeDepartment();
      this.address = address;
      if(address != null) address.setDepartment(this);
    }
    return this;
  }

  public Department removeAddress() {
    if(this.address != null){
      this.address.setDepartment(null);
      this.address = null;
    }
    return this;
  }

  public Department setEmployees(Set<Employee> employees) {
    if (employees != null) {
      this.employees = new HashSet<>(employees);
      employees.forEach(e -> e.setDepartment(this));
    }else{
      if (this.employees != null) {
        this.employees.forEach(Employee::removeDepartment);
        this.employees = null;
      }
    }
    return this;
  }

  public Department addEmployee(@NonNull Employee employee) {
    if (this.employees == null) {
      this.employees = new HashSet<>();
    }
    if(!this.employees.contains(employee)){
      this.employees.add(employee);
      employee.setDepartment(this);
    }
    return this;
  }

  public Department removeEmployee(@NonNull Employee employee) {
    if (this.employees == null) {
      return this;
    }
    if(this.employees.contains(employee)) {
      Department department = employee.getDepartment();
      department.getEmployees().stream().filter(t -> t.getDepartment() == this)
          .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
      this.employees.remove(employee);
      employee.setDepartment(null);
    }
    return this;
  }

  public Department removeEmployees() {
    if (this.employees != null) {
      this.employees.forEach(Employee::removeDepartment);
      this.employees = null;
    }
    return this;
  }

  public Set<Employee> getEmployees() {
    return Collections.unmodifiableSet(this.employees);
  }

  public Department setDepartmentHead(Employee departmentHead) {
    if(this.departmentHead != departmentHead) {
      if(this.departmentHead != null) this.departmentHead.removeDepartment();
      this.departmentHead = departmentHead;
      if(departmentHead != null) departmentHead.setDepartmentHeaded(this);
    }
    return this;
  }

  public Department removeDepartmentHead() {
    if(this.departmentHead != null){
      this.departmentHead.removeDepartment();
      this.departmentHead = null;
    }
    return this;
  }

  public Department setCompany(Company company) {
    if(this.company != company) {
      if(this.company != null) this.company.removeDepartment(this);
      this.company = company;
      if(company != null) company.addDepartment(this);
    }
    return this;
  }

  public Department removeCompany() {
    if(this.company != null){
      this.company.removeDepartment(this);
      this.company = null;
    }
    return this;
  }

  public Department setProjects(Set<Project> projects) {
    if (projects != null) {
      this.projectManagements = projects.stream().map(p -> {
            return new ProjectManagement()
                .setId(new ProjectManagementKey(this.getId(), p.getId()))
                .setDepartment(this)
                .setProject(p)
                .setRating(3);
          }
      ).collect(Collectors.toSet());
    }else{
      if(this.projectManagements != null) {
        this.projectManagements.forEach(pa -> {
          pa
              .setProject(null)
              .setDepartment(null);
        });
        this.projectManagements = null;
      }
    }
    return this;
  }

  public Department addProject(@NonNull Project project) {
    if (this.projectManagements == null) {
      this.projectManagements = new HashSet<>();
    }
    ProjectManagement projectManagement = new ProjectManagement()
        .setDepartment(this)
        .setProject(project)
        .setRating(3);
    project.setProjectManagement(projectManagement);
    this.projectManagements.add(projectManagement);
    return this;
  }

  public Department removeProject(@NonNull Project project) {
    if (this.projectManagements == null || project.getProjectManagement() == null) {
      return this;
    }
    ProjectManagement projectManagement = project.getProjectManagement();
    if(projectManagement.getDepartment() != this) throw new ResourceNotFoundException(); //association not found
    this.projectManagements.remove(projectManagement);
    projectManagement.setDepartment(null);
    return this;
  }

  public Set<Project> getProjects() {
    return this.projectManagements.stream()
        .map(ProjectManagement::getProject)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  public Department addProjectManagement(@NonNull ProjectManagement projectManagement) {
    if (this.projectManagements == null) {
      this.projectManagements = new HashSet<>();
    }
    if(!this.projectManagements.contains(projectManagement)){
      this.projectManagements.add(projectManagement);
      projectManagement.setDepartment(this);
    }
    return this;
  }

  public Department removeProjectManagement(@NonNull ProjectManagement projectManagement) {
    if (this.projectManagements == null) {
      return this;
    }
    if(this.projectManagements.contains(projectManagement)) {
      Department department = projectManagement.getDepartment();
      department.getProjectManagements().stream().filter(t -> t.getDepartment() == this)
          .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
      this.projectManagements.remove(projectManagement);
      projectManagement.setDepartment(null);
    }
    return this;
  }
  
}
