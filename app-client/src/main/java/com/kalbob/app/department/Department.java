package com.kalbob.app.department;

import com.kalbob.app.BaseEntity;
import com.kalbob.app.company.Company;
import com.kalbob.app.employee.Employee;
import com.kalbob.app.project.Project;
import com.kalbob.app.project.ProjectAssignment;
import com.kalbob.app.project.ProjectName;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  public Set<Employee> getEmployees() {
    return this.employees;
  }

  public Department setCompany(Company company){
    if(company != null) company.getDepartments().add(this);
    this.company = company;
    return this;
  }

  public Department removeCompany() {
    if(company != null) {
      this.company.removeDepartment(this);
      this.company = null;
    }
    return this;
  }

  public Department setDepartmentHead(Employee departmentHead){
    if(departmentHead != null) departmentHead.setDepartment(this);
    this.departmentHead = departmentHead;
    return this;
  }

  public Department setDepartmentHead_Internal(Employee departmentHead){
    this.departmentHead = departmentHead;
    return this;
  }

  public Department removeDepartmentHead() {
    if(this.departmentHead != null) {
      this.departmentHead.setDepartment(null);
      this.departmentHead = null;
    }
    return this;
  }
  
  /*public Department setProjects(List<Project> projects) {
    if (projects != null) {
      this.projectManagements = projects.stream().map(p -> {
        ProjectManagement pm = new ProjectManagement()
          .setDepartment(this)
          .setProject(p)
          .setRating(3);
        p.setProjectManagement(pm);//
        return pm;
      }).collect(Collectors.toSet());

    }else{
      if(this.projectManagements != null) {
        this.projectManagements.forEach(pa -> {
          if(pa.getProject() != null) {
            pa.getProject().setProjectManagement(null);
            pa.setDepartment(null);
          }
        });
        this.projectManagements = null;
      }
    }
    return this;
  }

  public Department addProject(@NonNull Project project) {
    if(this.projectManagements == null){
      this.projectManagements = new HashSet<>();
    }
    if(this.projectManagements.stream().anyMatch(pa -> pa.getProject()!=null && pa.getProject().equals(project))) {
      throw new ResourceNotFoundException();//Duplicate Exception, Already managing that project.
    }else{
      ProjectManagement projectManagement = new ProjectManagement()
          .setDepartment_Internal(this)
          .setProject(project)
          .setRating(3);
      this.projectManagements.add(projectManagement);
      project.setProjectManagement(projectManagement);
    }
    return this;
  }

  public Department removeProject(@NonNull Project project) {
    if (this.projectManagements == null || project.getProjectManagement() == null) {
      return this;
    }
    ProjectManagement projectManagement = project.getProjectManagement();
    if(projectManagement.getDepartment() != this) throw new ResourceNotFoundException(); //association not found i.e. this department does not manage that project
    this.projectManagements.remove(projectManagement);
    project.setProjectManagement(null);
    return this;
  }

  public List<Project> getProjects() {
    if(this.projectManagements != null) {
      return this.projectManagements.stream()
          .map(ProjectManagement::getProject)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }else {
      return new ArrayList<>();
    }
  }*/


  public Department setProjectManagements(List<ProjectManagement> projectManagements) {
    if (projectManagements != null) {
      projectManagements.forEach(e -> e.setDepartment(this));
      this.projectManagements = new HashSet<>(projectManagements);
    }else{
      removeProjectManagements();
    }
    return this;
  }

  public Set<ProjectManagement> getProjectManagements() {
    /*if(this.projectManagements != null) {
      return new ArrayList<>(projectManagements);
    }else {
      return new ArrayList<>();
    }*/
    return this.projectManagements;
  }

  public Department removeProjectManagements() {
    if (this.projectManagements != null) {
      this.projectManagements.forEach(e->e.setDepartment(null));//setDepartment(null));
      this.projectManagements = null;
    }
    return this;
  }

  public Department addProjectManagement(@NonNull ProjectManagement projectManagement) {
    if(this.projectManagements == null){
      this.projectManagements = new HashSet<>();
    }
    if(this.projectManagements.stream().noneMatch(pm -> pm.equals(projectManagement))) {
      this.projectManagements.add(projectManagement);
      projectManagement.setDepartment(this);
    }
    return this;
  }

  public Department removeProjectManagement(@NonNull ProjectManagement projectManagement) {
    if (this.projectManagements == null || projectManagement.getDepartment() == null) {
      return this;//exp not managing anything
    }
    if(!projectManagement.getDepartment().equals(this)) throw new ResourceNotFoundException(); //association not found i.e. this department does not manage that project
    this.projectManagements.remove(projectManagement);
    projectManagement.setDepartment(null);
    return this;
  }


  public static void main(String[] args){
    Department d = new Department();
    d.setType(DepartmentType.FINANCE);
    Address a = new Address();
    a.setStreet("tatum");
    Employee e1 = new Employee();
    e1.setFirstName("kal");
    Employee e2 = new Employee();
    e2.setFirstName("bob");
    Project p1 = new Project();
    p1.setName(ProjectName.RED);
    Project p2 = new Project();
    p2.setName(ProjectName.BLUE);

    //OnetoOne
    /*d.setAddress(a);
    a.setDepartment(d);
    a.removeDepartment();
    System.out.println("1 "+d.getAddress()+"-"+a.getDepartment());
    System.out.println("2 "+d.getAddress().equals(a));
    System.out.println("3 "+a.getDepartment().equals(d));
    System.out.println("----------------------------------------------------------------------------------");
    d.removeAddress();
    a.removeDepartment();
    System.out.println("1 "+d.getAddress()+"-"+a.getDepartment());
    System.out.println("2 "+d.getAddress().equals(a));
    System.out.println("3 "+a.getDepartment().equals(d));*/

    //OneToMany
    /*d.setEmployees(Arrays.asList(e1));
    //d.addEmployee(e1);
    //e1.setDepartment(d);
    System.out.println("1 "+d.getEmployees()+"-"+e1.getDepartment());
    System.out.println("2 "+d.getEmployees().contains(e1));
    System.out.println("3 "+e1.getDepartment().equals(d));
    System.out.println("----------------------------------------------------------------------------------");
    d.removeEmployees();
    //d.removeEmployee(e1);
    //e1.removeDepartment();
    System.out.println("1 "+d.getEmployees()+"-"+e1.getDepartment());
    System.out.println("2 "+d.getEmployees().contains(e1));
    System.out.println("3 "+e1.getDepartment().equals(d));*/

    //OneToMany Join Table
    /*ProjectManagement pm = new ProjectManagement();
    pm.setDepartment(d);
    pm.setProject(p1);
    //d.setProjectManagements(Arrays.asList(pm));
    //d.addProjectManagement(pm);
    //pm.setDepartment(d);
    //pm.setProject(p1);
    System.out.println("1 "+d.getProjectManagements()+"-"+pm);
    System.out.println("2 "+d.getProjectManagements().contains(pm));
    System.out.println("3 "+pm.getDepartment().equals(d));
    System.out.println("----------------------------------------------------------------------------------");
    d.removeProjectManagements();
    //d.removeProjectManagement(pm);
    //pm.removeProject();
    //pm.removeDepartment();
    System.out.println("1 "+d.getProjectManagements()+"-"+pm);
    System.out.println("2 "+d.getProjectManagements().contains(pm));
    System.out.println("3 "+pm.getDepartment().equals(d));*/

    //ManyToMany
    ProjectAssignment pa = new ProjectAssignment();
    pa.setProject(p1);
    pa.setEmployee(e1);
    p1.setProjectAssignments(Arrays.asList(pa));
    p1.addProjectAssignment(pa);
    e1.setProjectAssignments(Arrays.asList(pa));
    e1.addProjectAssignment(pa);
    System.out.println("1 "+pa.getEmployee() + "-" + p1.getProjectAssignments().iterator().next().getEmployee());
    System.out.println("2 "+pa.getProject() + "-" + p1.getProjectAssignments().iterator().next().getProject());
    System.out.println("3 "+pa.getProject().equals(p1.getProjectAssignments().iterator().next().getProject()));
    System.out.println("4 "+p1.getProjectAssignments().contains(pa));
    System.out.println("5 "+pa.getEmployee().equals(e1.getProjectAssignments().iterator().next().getEmployee()));
    System.out.println("6 "+e1.getProjectAssignments().contains(pa));
    System.out.println("----------------------------------------------------------------------------------");
    pa.removeProject();//Actual delete of pa & p
    //pa.removeEmployee();
    //p1.removeProjectAssignment(pa);//Soft delete i.e. meta-data change of pa & update p
    //e1.removeProjectAssignment(pa);
    System.out.println(pa);
    System.out.println(e1.getProjectAssignments());
    System.out.println(p1.getProjectAssignments());
    /*System.out.println("1 "+pa.getEmployee() + "-" + p1.getProjectAssignments().iterator().next().getEmployee());
    System.out.println("2 "+pa.getProject() + "-" + p1.getProjectAssignments().iterator().next().getProject());
    System.out.println("3 "+pa.getProject().equals(p1.getProjectAssignments().iterator().next().getProject()));
    System.out.println("4 "+p1.getProjectAssignments().contains(pa));
    System.out.println("5 "+pa.getEmployee().equals(e1.getProjectAssignments().iterator().next().getEmployee()));
    System.out.println("6 "+e1.getProjectAssignments().contains(pa));*/
  }

}
