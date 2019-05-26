package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"address", "employees"})
@EqualsAndHashCode(exclude = {"address", "employees"}, callSuper = true)
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

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.LAZY)
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

  public void removeAddress() {
    if(this.address != null){
      this.address.setDepartment(null);
    }
    this.address = null;
  }

  public Department setEmployees(List<Employee> employees) {
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

  public Department addEmployee(Employee employee) {
    if (this.employees != null && employee != null) {
      employee.setDepartment(this);
      this.employees.add(employee);
    }
    return this;
  }

  public Department removeEmployee(Employee employee) {
    if (this.employees != null && employee != null) {
      this.employees.remove(employee);
      employee.setDepartment(null);
    }
    return this;
  }

  public Department removeEmployees() {
    if (this.employees != null) {
      this.employees.forEach(e -> e.setDepartment(null));
      this.employees = null;
    }
    return this;
  }

  public List<Employee> getEmployees() {
    if (this.employees != null) {
      return new ArrayList<>(this.employees);
    } else {
      return new ArrayList<>();
    }
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

  public void removeDepartmentHead() {
    if(this.departmentHead != null){
      this.departmentHead.setDepartmentHeaded(null);
    }
    this.departmentHead = null;
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
    if(this.company != null){
      this.company.getDepartments().remove(this);
      this.company = null;
    }
    return this;
  }

  public Department setProjects(List<Project> projects) {
    if (projects != null) {
      this.managedProjects = projects.stream().map(p -> new ProjectManagement()
          .setProject(p)
          .setDepartment(this)
      ).collect(Collectors.toSet());
    }else{
      if(this.managedProjects != null) {
        this.managedProjects.forEach(pa -> {
          pa
              .setProject(null)
              .setDepartment(null);
        });
        this.managedProjects = null;
      }
    }
    return this;
  }

  public Department addProject(Project project) {
    ProjectManagement projectManagement = new ProjectManagement()
        .setDepartment(this)
        .setProject(project)
        .setRating(3)
        ;
    if (this.managedProjects != null) {
      this.managedProjects.add(projectManagement);
      project.setManagingDepartment(projectManagement);
    }
    return this;
  }

  public Department removeProject(Project project) {
    if (this.managedProjects != null) {
      ProjectManagement projectManagement = project.getManagingDepartment();
      if(projectManagement == null) throw new ResourceNotFoundException();
      //projectManagement.setDepartment(null); No, bcoz remove works off of equals.
      this.managedProjects.remove(projectManagement);
    }
    return this;
  }

  public List<Project> getProjects() {
    if (this.managedProjects != null) {
      return this.managedProjects.stream()
          .map(ProjectManagement::getProject)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }
  

}
