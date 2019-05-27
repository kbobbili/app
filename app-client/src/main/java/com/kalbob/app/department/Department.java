package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import java.time.LocalDate;
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
@ToString(exclude = {"employees", "managedProjects"})
@EqualsAndHashCode(exclude = {"employees", "managedProjects"}, callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "department")
public class Department extends BaseEntity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "company_id")
  private Company company;

  @Enumerated(EnumType.STRING)
  private DepartmentType type;

  private LocalDate startDate;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "department_head")
  public Employee departmentHead;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
  private Set<Employee> employees = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "id.department", fetch = FetchType.LAZY)
  private Set<ProjectManagement> managedProjects = new HashSet<>();

  public Department setAddress(Address address) {
    if(address != null) {
      address.setDepartment(this);
    }else{
      if(this.address != null){
        this.address.setDepartment(null);
      }
    }
    this.address = address;
    return this;
  }

  public Department setAddressUnidirectional(Address address) {
    this.address = address;
    return this;
  }

  public Department removeAddress() {
    if(this.address == null){
      return this;
    }
    this.address.setDepartment(null);
    this.address = null;
    return this;
  }

  public Department setEmployees(Set<Employee> employees) {
    if (employees != null) {
      employees.forEach(e -> e.setDepartment(this));
      this.employees = new HashSet<>(employees);
    }else{
      if (this.employees != null) {
        this.employees.forEach(e -> e.setDepartment(null));
        this.employees = null;
      }
    }
    return this;
  }

  public Department addEmployee(@NonNull Employee employee) {
    if (this.employees == null) {
      this.employees = new HashSet<>();
    }
    employee.setDepartment(this);
    this.employees.add(employee);
    return this;
  }

  public Department removeEmployee(@NonNull Employee employee) {
    if (this.employees == null) {
      return this;
    }
    Department department = employee.getDepartment();
    department.getEmployees().stream().filter(t -> t.getDepartment() == this)
        .findAny().orElseThrow(ResourceNotFoundException::new); //association not found
    this.employees.remove(employee);
    employee.setDepartment(null);
    return this;
  }

  public Department removeEmployees() {
    if (this.employees == null) {
      return this;
    }
    this.employees.forEach(e -> e.setDepartment(null));
    this.employees = null;
    return this;
  }

  public Set<Employee> getEmployees() {
    return this.employees;
  }

  public Department setDepartmentHead(Employee employee) {
    if(employee != null) {
      employee.setDepartmentHeaded(this);
    }else{
      if(this.departmentHead != null){
        this.departmentHead.setDepartmentHeaded(null);
      }
    }
    this.departmentHead = employee;
    return this;
  }

  public Department setDepartmentHeadUnidirectional(Employee employee) {
    this.departmentHead = employee;
    return this;
  }

  public Department removeDepartmentHead() {
    if(this.departmentHead == null) {
      return this;
    }
    this.departmentHead.setDepartmentHeaded(null);
    this.departmentHead = null;
    return this;
  }

  public Department setCompany(Company company) {
    if(company != null) {
      if(company.getDepartments() != null) company.getDepartments().add(this);
    }else{
      if(this.company != null){
        this.company.getDepartments().remove(this);
      }
    }
    this.company = company;
    return this;
  }

  public Department removeCompany() {
    if(this.company == null) {
      return this;
    }
    this.company.getDepartments().remove(this);
    this.company = null;
    return this;
  }

  public Department setProjects(Set<Project> projects) {
    if (projects != null) {
      this.managedProjects = projects.stream().map(p ->
          new ProjectManagement()
              .setId(new ProjectManagementKey()
                  .setDepartment(this)
                  .setProject(p))
              .setRating(3)
      ).collect(Collectors.toSet());
    }else{
      if(this.managedProjects != null) {
        this.managedProjects.forEach(pa -> {
          pa
              .getId()
              .setProject(null)
              .setDepartment(null);
        });
        this.managedProjects = null;
      }
    }
    return this;
  }

  public Department addProject(@NonNull Project project) {
    if (this.managedProjects == null) {
      this.managedProjects = new HashSet<>();
    }
    ProjectManagement projectManagement = new ProjectManagement()
        .setId(new ProjectManagementKey()
            .setDepartment(this)
            .setProject(project))
        .setRating(3);
    project.setManagingDepartment(projectManagement);
    this.managedProjects.add(projectManagement);
    return this;
  }

  public Department removeProject(@NonNull Project project) {
    if (this.managedProjects == null || project.getManagingDepartment() == null) {
      return this;
    }
    ProjectManagement projectManagement = project.getManagingDepartment();
    if(projectManagement.getId().getDepartment() != this) throw new ResourceNotFoundException(); //association not found
    this.managedProjects.remove(projectManagement);
    projectManagement.getId().setDepartment(null);
    return this;
  }

  public Set<Project> getProjects() {
    return this.managedProjects.stream()
        .map(pm -> pm.getId().getProject())
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }
  

}
