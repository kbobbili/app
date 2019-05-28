package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"employees", "projectManagements"})
@EqualsAndHashCode(exclude = {"employees", "projectManagements"}, callSuper = false)
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


  public Department setAddress(Address address){
    if(address != null) address.setDepartment(this);
    this.address = address;
    return this;
  }

  public Department setAddress_Internal(Address address){
    this.address = address;
    return this;
  }

  public Department removeAddress() {
    if(this.address != null) {
      this.address.setDepartment(null);
      this.address = null;
    }
    return this;
  }

  public Department setEmployees(List<Employee> employees) {
    if (employees != null) {
      employees.forEach(e -> e.setDepartment(this));
      this.employees = new HashSet<>(employees);
    }else{
      removeEmployees();
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
    if (this.employees != null) {
      if (!this.employees.contains(employee))
        throw new ResourceNotFoundException();
      this.employees.remove(employee);
      employee.setDepartment(null);
    }
    return this;
  }

  public Department removeEmployees() {
    if (this.employees != null) {
      this.employees.forEach(e->e.setDepartment(null));
      this.employees = null;
    }
    return this;
  }

  public List<Employee> getEmployees() {
    if(this.employees != null) {
      return new ArrayList<>(this.employees);
    }else{
      return new ArrayList<>();
    }
  }

  //-------------

  public Department setProjects(Set<Project> projects) {
    if (projects != null) {
      this.projectManagements = projects.stream().map(p -> {
            return new ProjectManagement()
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
        .map(pm -> pm.getProject())
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  public static void main(String[] args){
    Department d = new Department();
    d.setType(DepartmentType.FINANCE);
    Address a = new Address();
    a.setStreet("tatum");

    /*d.setAddress(a);
    System.out.println("---1----");
    System.out.println(d.getAddress());
    System.out.println(a.getDepartment());*/

    /*a.setDepartment(d);
    System.out.println("---2----");
    System.out.println(d.getAddress());
    System.out.println(a.getDepartment());*/

    /*d.setAddress(a);
    a.removeDepartment();
    System.out.println("---3----");
    System.out.println(d.getAddress());
    System.out.println(a.getDepartment());*/

    /*System.out.println("---4----");
    System.out.println(d.getAddress());
    System.out.println(a.getDepartment());*/


    Employee e1 = new Employee();
    e1.setFirstName("kal");

    Employee e2 = new Employee();
    e2.setFirstName("bob");

    Employee e3 = new Employee();
    e3.setFirstName("axl");

    d.setEmployees(Arrays.asList(e1, e2));
    //d.addEmployee(e1);
    //e1.setDepartment(d);
    //d.removeEmployee(e1);
    //e1.removeDepartment();
    d.removeEmployees();
    System.out.println(d.getEmployees());
    System.out.println(e1.getDepartment());
  }

}
